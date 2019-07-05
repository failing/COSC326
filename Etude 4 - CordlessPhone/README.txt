Group:

Ben Highsted 5536340
Bradley Carter 8277205
Matthew Neil 7388248
Maurice Andrews 8833639


Instructions:
	Extract the file into a single folder
	
Compile using:
	javac *.java

Run using:
	java Cordless < input.txt

In our submission we went through many issues of what the most efficiently and sane way of trying to calculate the maximum range
for all our points. If we iterated through n points, for each point x, we draw a circle inclusive of x and 10 (so 11 in total) points closet to x.
We do this for every x,y cord in n. Doing this we are efficiently calculating the radius of every single point(plus 10 others to complete the circle)
and finding the smallest radius. We are finding the smallest radius because that is the actually the maximum range possible. As if we plot that circle anywhere
on the plane, we know that it will not encompass more than 11 points.  

We made use of two libraries, one being SmallestEncodingCircle, which calculates the centre x and y points for n points.

We also made use of a k-d tree which is a data structure used for storing points on a continuous space/plane.

It was useful as it is efficient at visiting/finding the nearest n points from x point. This was needed as we needed to visit every point in the dataset to construct our maximum range.


