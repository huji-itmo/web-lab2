package structs;
import java.io.Serializable;

import exceptions.BadParameterException;
import exceptions.MissingParametersException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestData implements Serializable {

    Double x;
    Double y;
    Integer r;    

    public void throwIfBadData() throws MissingParametersException, BadParameterException {
        throwIfXYRParametersEmpty();
        checkYValue();
        checkXValue();
        checkRValue();
    }

    private void throwIfXYRParametersEmpty() throws MissingParametersException{

        String errorString = "Missing parameters:";
        boolean hasMissingParameter = false;

        if (x == null) {
            hasMissingParameter = true;
            errorString += " \"x\"";
        }
        
        if (y == null) {
            hasMissingParameter = true;
            errorString += " \"y\"";
        }

        if (r == null) {
            hasMissingParameter = true;
            errorString += " \"r\"";
        }

        if (hasMissingParameter) {
            throw new MissingParametersException(errorString);
        }
    }

    private int checkRValue() throws BadParameterException {
        if (r < 1 || r > 5) {
            throw new BadParameterException("R parameter must be in range [1,5]");
        }

        return r;
    }

    private double checkYValue() throws BadParameterException {
        if (y < -3.0D || y > 3.0D) {
            throw new BadParameterException("Y parameter must be in range [-3,3]");
        }

        return y;
    }

    private double checkXValue() throws BadParameterException {
        if (x < -2.0D || x > 2.0D) {
            throw new BadParameterException("X parameter must be in range [-2,2]");
        }

        boolean isGood = false;

        for (double num = -2.0D; num < 2.5D; num += 0.5D) {
            if (x == num) {
                isGood = true;
                break;
            }
        }

        if (!isGood) {
            throw new BadParameterException("X parameter must be a multiple of 0.5");
        }

        return x;
    }
}