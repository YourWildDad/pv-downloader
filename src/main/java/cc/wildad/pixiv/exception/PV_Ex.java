package cc.wildad.pixiv.exception;

public class PV_Ex {

    public static PV_Exception newConnEx(Throwable e, String format, Object... obj) {
        return new PV_ConnException(String.format(format, obj), e);
    }

    public static PV_Exception newQueryEx(Throwable e, String format, Object... obj) {
        return new PV_QueryException(String.format(format, obj), e);
    }

    public static PV_Exception newEx(Throwable e, String format, Object... obj) {
        return new PV_QueryException(String.format(format, obj), e);
    }
}
