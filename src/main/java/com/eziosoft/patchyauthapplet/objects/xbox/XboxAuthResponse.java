package com.eziosoft.patchyauthapplet.objects.xbox;

public class XboxAuthResponse {

    private String IssueInstant;
    private String NotAfter;
    private String Token;
    private XboxDisplayClaims DisplayClaims;

    public String getIssueInstant() {
        return IssueInstant;
    }

    public String getNotAfter() {
        return NotAfter;
    }

    public String getToken() {
        return Token;
    }

    public XboxDisplayClaims getDisplayClaims() {
        return DisplayClaims;
    }

    // apparently we need subclasses for this shit lmao
    public class XboxXUI {
        private String uhs;

        public String getUhs() {
            return uhs;
        }
    }
    public class XboxDisplayClaims {
        private XboxXUI[] xui;

        public XboxXUI[] getXui() {
            return xui;
        }
    }
}
