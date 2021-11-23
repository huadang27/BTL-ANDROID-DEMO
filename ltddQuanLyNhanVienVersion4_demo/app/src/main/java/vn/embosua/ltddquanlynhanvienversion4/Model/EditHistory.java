package vn.embosua.ltddquanlynhanvienversion4.Model;

public class EditHistory {
    String DateTime;// thoi gian sua chua
    String IDStaff; // id nhan vien bi sua thong tin
    String IDEditor;// id nguoi da sua thong tin
    String Reason; // ly do chinh sua
    Staff InfoCorrected; // thong tin da sua

    public EditHistory() {
    }

    public EditHistory(String dateTime, String IDStaff, String IDEditor, String reason, Staff infoCorrected) {
        DateTime = dateTime;
        this.IDStaff = IDStaff;
        this.IDEditor = IDEditor;
        Reason = reason;
        InfoCorrected = infoCorrected;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getIDStaff() {
        return IDStaff;
    }

    public void setIDStaff(String IDStaff) {
        this.IDStaff = IDStaff;
    }

    public String getIDEditor() {
        return IDEditor;
    }

    public void setIDEditor(String IDEditor) {
        this.IDEditor = IDEditor;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public Staff getInfoCorrected() {
        return InfoCorrected;
    }

    public void setInfoCorrected(Staff infoCorrected) {
        InfoCorrected = infoCorrected;
    }
}
