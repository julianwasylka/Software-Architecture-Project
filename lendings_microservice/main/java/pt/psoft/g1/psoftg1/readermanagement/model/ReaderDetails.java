package pt.psoft.g1.psoftg1.readermanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "READER_DETAILS")
public class ReaderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Getter
    @Setter
    @OneToOne
    private Reader reader;

    private ReaderNumber readerNumber;

    @Embedded
    @Getter
    private BirthDate birthDate;

    @Embedded
    private PhoneNumber phoneNumber;

    @Setter
    @Getter
    @Basic
    private boolean gdprConsent;

    @Setter
    @Basic
    @Getter
    private boolean marketingConsent;

    @Setter
    @Basic
    @Getter
    private boolean thirdPartySharingConsent;

    @Getter
    @Setter
    @ManyToMany
    private List<Genre> interestList;

    public ReaderDetails(int readerNumber, Reader reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, List<Genre> interestList) {
        if(reader == null || phoneNumber == null)
            throw new IllegalArgumentException("Provided argument resolves to null object");

        if(!gdpr)
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");

        setReader(reader);
        setReaderNumber(new ReaderNumber(readerNumber));
        setPhoneNumber(new PhoneNumber(phoneNumber));
        setBirthDate(new BirthDate(birthDate));
        setGdprConsent(true);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);
        setInterestList(interestList);
    }

    private void setPhoneNumber(PhoneNumber number) {
        if (number != null)
            this.phoneNumber = number;
    }

    private void setReaderNumber(ReaderNumber readerNumber) {
        if (readerNumber != null)
            this.readerNumber = readerNumber;
    }

    private void setBirthDate(BirthDate date) {
        if (date != null)
            this.birthDate = date;
    }

    public String getReaderNumber(){ return this.readerNumber.toString();}
    public String getPhoneNumber() { return this.phoneNumber.toString();}

    public ReaderDetails() {}
}
