package kr.co.wisenut.common.Exception;
import java.sql.SQLException;

/**
 * Created by KoreaWISENut
 * User: KoreaWisenut
 * Date: 2005. 5. 16.
 * Time: 오후 4:52:31
 */

public class DBFactoryException extends Exception{
    protected StringBuffer message = new StringBuffer();
    protected Throwable throwable = null;

    public DBFactoryException(){
    };

    public DBFactoryException(String exp){
        message.append(exp);
    }

    public DBFactoryException(Throwable throwable) {
        this("", throwable);
    }

    public DBFactoryException(String message, Throwable throwable) {
        super();
        this.message.append( message );
        this.throwable = throwable;
    }

    public synchronized void handlelSQLException(String query, SQLException se){
        message.append("SQLExeption\n");
        message.append("QUERY: " + query);
        message.append("1. 쿼리문에 오류가 있어 정상적으로 수행되지 않았습니다.");
    }

    public synchronized void handlelClassNotFoundException(String driver, ClassNotFoundException ne){
        message.append("ClassNotFoundException\n");
        message.append("JDBC DRIVER: " + driver);
        message.append(" 1. DBMS에 해당하는 JDBC Driver를 찾을 수 없습니다..");
        message.append(" 환경변수 CLASSPATH에 해당 드라이버를 설정하시기 바랍니다.");
    }

    public String toString(){
        if(throwable != null) {
            message.append(throwable.toString());
        }
        return (message.toString());
    }
}
