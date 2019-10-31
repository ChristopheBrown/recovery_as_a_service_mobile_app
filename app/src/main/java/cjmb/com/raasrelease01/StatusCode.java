package cjmb.com.raasrelease01;

/*The server returns status codes for their transactions. They're using for taking action based
* on an request's completion. This class is an abstraction to make code readability better.
*
* The codes correspond to action HTTP request response codes.*/

public interface StatusCode {
        int SUCCESS = 200;
        int UNAUTHORIZED = 401;
        int FORBIDDEN = 403;
        int NOTFOUND = 404;
}
