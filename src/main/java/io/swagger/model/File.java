package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * File
 */
@Entity
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-27T07:39:21.717Z[GMT]")

public class File   {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonProperty("id")
  @NotBlank(message = "Id is mandatory")
  private Long id = null;

  @Column(nullable = false)
  @JsonProperty("name")
  private String name = null;

  @Column(nullable = true)
  @JsonProperty("descr")
  private String descr = null;

  @Column(nullable = true)
  @JsonProperty("virus")
  private Boolean virus = null;

  @Column(nullable = true)
  @JsonProperty("ownedBy")
  private Long ownedBy = null;

  @Column(nullable = true)
  @JsonProperty("signature")
  private String signature = null;

  @Column(nullable = true)
  @JsonProperty("extension")
  private String extension = null;

  public File id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public File name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the file
   * @return name
   **/
  @Schema(required = true, description = "Name of the file")
      @NotNull

    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Extension of the file
   * @return extension
   **/
  @Schema(required = true, description = "Extension of the file")
  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public File descr(String descr) {
    this.descr = descr;
    return this;
  }

  /**
   * Short description of what the file is about
   * @return descr
   **/
  @Schema(description = "Short description of what the file is about")
  
    public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public File virus(Boolean virus) {
    this.virus = virus;
    return this;
  }

  /**
   * Determines whether file uploaded is virus
   * @return virus
   **/
  @Schema(description = "Determines whether file uploaded is virus")
  
    public Boolean isVirus() {
    return virus;
  }

  public void setVirus(Boolean virus) {
    this.virus = virus;
  }

  public File ownedBy(Long ownedBy) {
    this.ownedBy = ownedBy;
    return this;
  }

  /**
   * Determines the user Id of the file owner
   * @return ownedBy
   **/
  @Schema(description = "Determines the user Id of the file owner")
  
    public Long getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(Long ownedBy) {
    this.ownedBy = ownedBy;
  }

  public File signature(String signature) {
    this.signature = signature;
    return this;
  }

  /**
   * Digital Signature created by the file owner.
   * @return signature
   **/
  @Schema(description = "Digital Signature created by the file owner.")
  
    public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    File file = (File) o;
    return Objects.equals(this.id, file.id) &&
        Objects.equals(this.name, file.name) &&
        Objects.equals(this.descr, file.descr) &&
        Objects.equals(this.virus, file.virus) &&
        Objects.equals(this.ownedBy, file.ownedBy) &&
        Objects.equals(this.signature, file.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, descr, virus, ownedBy, signature);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class File {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    descr: ").append(toIndentedString(descr)).append("\n");
    sb.append("    virus: ").append(toIndentedString(virus)).append("\n");
    sb.append("    ownedBy: ").append(toIndentedString(ownedBy)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
