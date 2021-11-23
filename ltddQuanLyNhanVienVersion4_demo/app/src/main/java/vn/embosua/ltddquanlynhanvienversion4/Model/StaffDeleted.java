package vn.embosua.ltddquanlynhanvienversion4.Model;

public class StaffDeleted {
    String DateTime;// thoi gian xoa
    String IDEditor;// id nguoi da xoa nhan vien
    Staff StaffInfor; // thong tin da xoa
    String Reason; // ly do xoa nhan vien

    public StaffDeleted() {
    }

    public StaffDeleted(String dateTime, String IDEditor, Staff staffInfor, String reason) {
        DateTime = dateTime;
        this.IDEditor = IDEditor;
        StaffInfor = staffInfor;
        Reason = reason;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getIDEditor() {
        return IDEditor;
    }

    public void setIDEditor(String IDEditor) {
        this.IDEditor = IDEditor;
    }

    public Staff getStaffInfor() {
        return StaffInfor;
    }

    public void setStaffInfor(Staff staffInfor) {
        StaffInfor = staffInfor;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
