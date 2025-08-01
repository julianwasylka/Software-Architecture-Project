package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateReaderRequest {
    @Setter
    @Getter
    @Nullable
    private String number;

    @Getter
    @Setter
    @Nullable
    private String username;

    @Getter
    @Setter
    @Nullable
    private String password;

    @Getter
    @Nullable
    private String fullName;

    @Getter
    @Nullable
    private String birthDate;

    @Getter
    @Nullable
    private String phoneNumber;

    @Nullable
    private boolean marketing;

    @Nullable
    private boolean thirdParty;

    @Nullable
    @Getter
    @Setter
    private List<String> interestList;

    public boolean getThirdParty() {
        return thirdParty;
    }

    public boolean getMarketing() {
        return marketing;
    }
}
