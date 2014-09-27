import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by rafaelrs on 27.09.14.
 */
public class rectangleCalc {

    static class Rectangle {
        public int left, top, right, bottom;

        Rectangle(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public String toString() {
            return "(" + String.valueOf(left) + ", " + String.valueOf(top) + "; " + String.valueOf(right) + ", " + String.valueOf(bottom) + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!Rectangle.class.isInstance(obj)) return false;

            Rectangle otherRect = (Rectangle)obj;
            return (left == otherRect.left) && (top == otherRect.top) && (right == otherRect.right) && (bottom == otherRect.bottom);
        }
    }

    public static void main(String[] args) {
        //Create array with rectangles
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        //rectangles.add(new Rectangle(0, 1, 3, 3));
        //rectangles.add(new Rectangle(2, 2, 6, 4));
        //rectangles.add(new Rectangle(1, 0, 3, 5));

        System.out.println("Enter rectangles data. Use format: left top right bottom");
        System.out.println("Example: 0 1 3 3");
        while (true) {
            try {
                System.out.println("Rectangle " + String.valueOf(rectangles.size() + 1) + " ([ENTER] to finish)");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String inData = in.readLine();

                if (inData.isEmpty()) break;

                String[] numStrings = inData.split(" ");
                if (numStrings.length != 4) {
                    System.out.println("Input ERROR. There should be 4 numbers");
                    break;
                }

                rectangles.add(new Rectangle(Integer.parseInt(numStrings[0]), Integer.parseInt(numStrings[1]), Integer.parseInt(numStrings[2]), Integer.parseInt(numStrings[3])));

            } catch (IOException e) {
                System.out.println("Input ERROR!");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Input ERROR! Wrong number format");
                return;
            }

        }

        validateRectangles(rectangles);

        int areaSize = calculateArea(rectangles);

        // Printing result
        System.out.println("Given rectangles below:");
        System.out.println("  " + rectangles.toString());
        System.out.println();
        System.out.println("Calculated area size: " + String.valueOf(areaSize));

    }

    /**
     * Validates then rectangle's left always less then right and top always less than bottom
     *
     * @param rectangles to validate
     */
    private static void validateRectangles(ArrayList<Rectangle> rectangles) {

        for (Rectangle rect : rectangles) {
            if (rect.left > rect.right) {
                int left = rect.right;
                rect.right = rect.left;
                rect.left = left;
            }

            if (rect.top > rect.bottom) {
                int top = rect.bottom;
                rect.bottom = rect.top;
                rect.top = top;
            }
        }

    }

    /**
     * Calculates area size of given rectangles taking account their intersections
     *
     * @param rectangles to calculate
     * @return area size of rectangles
     */
    private static int calculateArea(ArrayList<Rectangle> rectangles) {

        //If input data empty return zero
        if (rectangles == null || rectangles.size() == 0) return 0;

        //Two duplicate rectangles equals to one rectangle area size
        removeDuplicates(rectangles);

        int areasSum = 0;

        //Calculating all rectangles area sizes
        for (Rectangle rec : rectangles) areasSum += (rec.right - rec.left) * (rec.bottom - rec.top);

        //If we have more than one rectangle, let's check for intersections and exclude
        //intersected areas from area summary size
        if (rectangles.size() > 1) {
            ArrayList<Rectangle> intersectingRecs = findIntersections(rectangles);
            areasSum -= calculateArea(intersectingRecs);
        }

        return areasSum;
    }

    /**
     * Finds intersecting rectangles and creates array with them
     *
     * @param rectangles to check
     * @return intersecting rectangles array
     */
    private static ArrayList<Rectangle> findIntersections(ArrayList<Rectangle> rectangles) {

        ArrayList<Rectangle> intersectingRecs = new ArrayList<Rectangle>();

        for (int i = 0; i < rectangles.size(); i++) {
            for (int j = i + 1; j < rectangles.size(); j++) {
                Rectangle firstRect = rectangles.get(i);
                Rectangle secondRect = rectangles.get(j);

                //Get measurements for potential rectangle
                int newLeft = Math.max(firstRect.left, secondRect.left);
                int newTop = Math.max(firstRect.top, secondRect.top);
                int newRight = Math.min(firstRect.right, secondRect.right);
                int newBottom = Math.min(firstRect.bottom, secondRect.bottom);

                if (newLeft < newRight && newTop < newBottom)
                    intersectingRecs.add(new Rectangle(newLeft, newTop, newRight, newBottom));
            }
        }

        return intersectingRecs;
    }



    /**
     * Removes all rectangle duplicates from array
     *
     * @param rectangles
     */
    private static void removeDuplicates(ArrayList<Rectangle> rectangles) {
        //Copying source data to other array
        ArrayList<Rectangle> oldRectangles = (ArrayList<Rectangle>)rectangles.clone();
        rectangles.clear();

        //Add rectangle if we hadn't added it's duplicate before
        for (Rectangle rect : oldRectangles) {

            boolean isDuplicate = false;
            for (Rectangle rectToCheck : rectangles) {
                if (rectToCheck.equals(rect)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) rectangles.add(rect);
        }
    }
}
