package pt.psoft.g1.psoftg1.readermanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "Reader Details for AMQP communication")
@AllArgsConstructor
@NoArgsConstructor
public class ReaderDetailsViewAMQP {
    private String username;
    private String password;
    private String fullName;
    private String readerNumber;
    private String birthDate;
    private String phoneNumber;
    private Boolean gdprConsent;
    private Boolean marketingConsent;
    private Boolean thirdPartySharingConsent;
    private List<String> interestList;
}
