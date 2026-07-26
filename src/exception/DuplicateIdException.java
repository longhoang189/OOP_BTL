package exception;

public class DuplicateIdException extends Exception {
    public DuplicateIdException(String mssv) {
        super("MSSV '" + mssv + "' đã tồn tại trong hệ thống.");
    }
}
