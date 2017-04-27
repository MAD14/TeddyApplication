package it.polito.teddyapplication;

/**
 * Created by Utente on 27/04/2017.
 */

public class InviteMail extends Mail {
    private String message = "Hello! \n" +
            "You received an invite to join a group in TeddyApplication from one of your friend.\n" +
            "Lets join our community downloading our app at this link:\n" +
            "https://teddyapplication.com/welcome\n" +
            "Your MAD14 team";
    private String _body;

    public InviteMail(){
        super();
        this._body=message;
    }

    public String get_body() {
        return _body;
    }
}
