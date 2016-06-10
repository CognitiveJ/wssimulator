package wssimulator

/**
 * Created by Ian on 10/06/2016.
 */
class TestUtils {
    static Random rand = new Random()

    static int randomPort() {
        rand.nextInt(64000) + 1024
    }
}
