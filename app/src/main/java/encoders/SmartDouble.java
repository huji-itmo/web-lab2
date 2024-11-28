package encoders;
import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import exceptions.CantAcceptNaNsException;

public class SmartDouble extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        out.value(value);
    }

    @Override
    public Double read(JsonReader in) throws IOException {

        String readString = in.nextString();

        Double parsedDouble = Double.parseDouble(readString);

        if (Double.isNaN(parsedDouble)) {
            throw new CantAcceptNaNsException("Program can't accept NaN value.");
        }

        String parsedString = Double.toString(parsedDouble);

        if (readString.length() > parsedString.length()) {
            int i = parsedString.length();
            while (readString.length() > i) {
                if (readString.charAt(i) != '0') {
                    return parsedDouble += 0.00001D * Double.compare(parsedDouble, 0F);
                }
                i++;
            }
        }
        return parsedDouble;
    }
}