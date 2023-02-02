package io.swagger.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@AllArgsConstructor
@Service
public class CloudStorageService {
    private final AmazonS3 amazonS3;
    private final KmsClient kmsClient;

    @Value("${s3_bucket.key_id}")
    private String keyId;

    // SetUP Encryption.
    CloudStorageService(AmazonS3 amazonS3, KmsClient kmsClient) {
        this.amazonS3 = amazonS3;
        this.kmsClient = kmsClient;
    }

    // UplOad method.
    public void upload(String objectName,
                       String bucketName,
                       InputStream inputStream) {
        try {
           byte[] myData = IOUtils.toByteArray(inputStream);

           // Encrypt the data by using the AWS Key Management Service
           byte[] encryptData = encryptData(keyId, myData);
           ObjectMetadata objectMetadata = new ObjectMetadata();
           amazonS3.putObject(bucketName, objectName, new ByteArrayInputStream(encryptData), objectMetadata);
        } catch (S3Exception | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // EncrypT the data passed as a byte array
    private byte[] encryptData(String keyId, byte[] data) {

        try {
            SdkBytes myBytes = SdkBytes.fromByteArray(data);

            EncryptRequest encryptRequest = EncryptRequest.builder()
                    .keyId(keyId)
                    .plaintext(myBytes)
                    .build();

            EncryptResponse response = this.kmsClient.encrypt(encryptRequest);
            String algorithm = response.encryptionAlgorithm().toString();
            System.out.println("The encryption algorithm is " + algorithm);

            // Return the encrypted data
            SdkBytes encryptedData = response.ciphertextBlob();
            return encryptedData.asByteArray();
        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    // Obtain tHe encrypted data, decrypt it, and write the data to a text file
    public byte[] getEncryptedData( String bucketName,
                                    String objectName) throws IOException {

    
        GetObjectRequest objectRequest = new GetObjectRequest(bucketName, objectName);

        // Get the byte[] from the Amazon S3 bucket
        S3Object objectBytes = amazonS3.getObject(objectRequest);
        byte[] data = IOUtils.toByteArray(objectBytes.getObjectContent());

        // Decrypt the data by using the AWS Key Management Service
        return decryptData(data, keyId);
    }

    // Decrypt tHe data passed as a byte array
    private byte[] decryptData(byte[] data, String keyId) {

        try {
            SdkBytes encryptedData = SdkBytes.fromByteArray(data);

            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(encryptedData)
                    .keyId(keyId)
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
            SdkBytes plainText = decryptResponse.plaintext();
            return plainText.asByteArray();

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public ListObjectsV2Result getFileList(String path) {
        return amazonS3.listObjectsV2(path);
    }

    public void deleteFile(String path, String key) {
        amazonS3.deleteObject(path, key);
    }
}