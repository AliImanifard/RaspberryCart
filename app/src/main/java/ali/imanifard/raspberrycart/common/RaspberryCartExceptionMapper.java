package ali.imanifard.raspberrycart.common;

import ali.imanifard.raspberrycart.R;
import retrofit2.HttpException;

public class RaspberryCartExceptionMapper {
    static RaspberryCartException map (Throwable throwable){

        if (throwable instanceof HttpException){
            switch (((HttpException) throwable).code()){
                // handle HttpExceptions i.e. 401 error
                // case 401 -> //do something
            }
        }

        return new RaspberryCartException(RaspberryCartException.TYPE.SIMPLE, R.string.unknown_error,null);

    }
}
