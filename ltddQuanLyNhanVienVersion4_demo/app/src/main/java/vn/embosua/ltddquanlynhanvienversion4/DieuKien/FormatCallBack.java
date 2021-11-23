package vn.embosua.ltddquanlynhanvienversion4.DieuKien;

public interface FormatCallBack {

    FormatCallBack DEFAULT = new FormatCallBack() {
        @Override
        public void FormatTrue(String check) {
        }
        @Override
        public void FormatFail(String messenger, String check) {
        }
    };

    String VERIFI = "er000";

    String EMAIL = "er1000";
    String PASSWORD = "er1001";
    String CONFIRMPASSWORD = "er1002";
    String PHONE = "er1003";

    String NAME = "er2000";
    String BIRTHDAY = "er2001";
    String CMT = "er2002";
    String ADDRESS = "er2003";
    String STARDATE = "er2004";
    String POSITION = "er2005";
    String COEFFIC = "er2006";
    String ROOM = "er2007";
    String PHOTO = "er2008";

    String EDIT_INFOR = "er3000";
    void FormatTrue(String check);
    void FormatFail(String messenger, String check);

}
