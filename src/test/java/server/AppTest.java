package server;

import org.junit.Test;
import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void shouldLoadCourses() throws IOException {
        Server server = new Server(0);

        ArrayList<Course> actualResult = server.getCoursesFromFile();

        assertEquals(6, actualResult.size());
    }
}
