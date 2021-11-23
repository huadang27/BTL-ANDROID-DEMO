package vn.embosua.ltddquanlynhanvienversion4.DuLieu;

import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;

public interface FbCallback {
    FbCallback DEFAULT = new FbCallback() {
        @Override
        public void QuerySuccess(List<Object> object, String check, int i, int j) {
        }
        @Override
        public void QueryFail(String mess, String check) {
        }
    };

    String STAFF = "staff1000";
    String LIST_STAFF = "staff1001";
    String CALENDER = "calender1000";
    String LIST_CALENDER = "calender1001";

// i - day work, j - day off
    void QuerySuccess(List<Object> object, String check, int i, int j);
    void QueryFail(String mess, String check);

}
