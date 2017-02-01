package Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sam on 1/31/2017.
 */
@IgnoreExtraProperties
public class Kid {
    public String names;
    public String kidemail;
    public String email;
    public Kid() {
    }

    public Kid (String names, String email, String kidemail) {
        this.names = names;
        this.email = email;
        this.kidemail = kidemail;
    }

    public String getNames() {
        return names;
    }

    public String getKidemail() {
        return kidemail;
    }

    public String getEmail() {
        return email;
    }
}
