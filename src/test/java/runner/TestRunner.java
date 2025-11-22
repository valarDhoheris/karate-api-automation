package runner;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.Test;


public class TestRunner {

    @Test
    public void testParallel() {
        Results results = Runner.path("classpath:features")
                //.tags("@regression") // specify the tag to filter scenarios
                .outputCucumberJson(true) // generate Cucumber JSON report
                .parallel(5); // run tests in parallel with 5 threads
    }
}
