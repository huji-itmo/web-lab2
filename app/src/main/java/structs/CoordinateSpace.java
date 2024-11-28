package structs;

public class CoordinateSpace {
    
    public static boolean testHit(RequestData data) {

        if (data.y >= 0) {

            double upperLimit = topBatman(data.x, data.r);

            return upperLimit >= data.y;  

        } else {

            double bottomLimit = bottomBatman(data.x, data.r);

            return bottomLimit <= data.y;  
        }
    }

        /* https://www.desmos.com/calculator/umrkccha3l */
    private static double topBatman(double xValue, double r) {
        double x = Math.abs(xValue);
        double y = 0;
        if(x<(r)/(14)) {
            y=(9*r)/(28);
        }
        else if (x<(3*r)/(28)) {
            y=3*x+(3*r)/(28);
        }
        else if (x<r/7) {
            y=((9*r)/(7))-8*x;
        }
        else if (x<(r*3)/(7)) {
            y=((3*r)/14)-x/2-((6*Math.sqrt(10))/14)*(Math.sqrt(3*Math.pow((r/7),2) - Math.pow(x,2) + (2*x*r)/(7)) - (2*r)/(7));
        }
        else if(x<r) {
            y=((r*3)/(7)) * Math.sqrt(-Math.pow(((x)/(r)),2)+1);
        } 
        return y;
    }

        
    private static double bottomBatman(double xValue, double r) {
        double x = Math.abs(xValue);
        double y = 0;

        if (x<(r*4)/(7)) {
            y=x/2 - ((r*(3*Math.sqrt(33)-7))/(784))*Math.pow(((7*x)/r),2) + (r/7) * Math.sqrt(1 -Math.pow(Math.abs((7*x/r)-2)-1,2))-(r*3)/7;
        }
        
        else if (x<r) {
            y=-((r*3)/7)*Math.sqrt(-Math.pow(((x)/(r)),2) + 1);
        }
        
        return y;
    }
}
