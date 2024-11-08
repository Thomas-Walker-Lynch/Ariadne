
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;

import com.ReasoningTechnology.Ariadne.Ariadne_File;


public class Test_File_0 {

    public class TestSuite {

        public Boolean unpack_file_path_0(Mosaic_IO io) {
            Boolean[] conditions = new Boolean[5];
            int i = 0;

            String test_fp = "/home/user/test.txt";
            String expected_dp = "/home/user/";
            String expected_fn = "test.txt";
            String expected_fn_base = "test";
            String expected_fn_ext = "txt";

            try {
                Map<String, String> result = Ariadne_File.unpack_file_path(test_fp);

                conditions[i++] = result.get("dp").equals(expected_dp);
                conditions[i++] = result.get("fn").equals(expected_fn);
                conditions[i++] = result.get("fn_base").equals(expected_fn_base);
                conditions[i++] = result.get("fn_ext").equals(expected_fn_ext);
                conditions[i++] = result.size() == 4;

                return Mosaic_Util.all(conditions);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public Boolean file_exists_q_0(Mosaic_IO io) {
            Boolean[] conditions = new Boolean[2];
            int i = 0;

            String repoHome = System.getenv("REPO_HOME");
            String existingFilePath = repoHome + "/tester/data_Test_File_0/I_exist";
            String nonExistentFilePath = repoHome + "/tester/data_Test_File_0/I_do_not_exist";

            conditions[i++] = Ariadne_File.file_exists_q(existingFilePath);
            conditions[i++] = !Ariadne_File.file_exists_q(nonExistentFilePath);

            return Mosaic_Util.all(conditions);
        }

        public Boolean newer_than_all_0(Mosaic_IO io) throws IOException {
            Boolean[] conditions = new Boolean[5];
            int i = 0;

            String repoHome = System.getenv("REPO_HOME");
            String file_0 = repoHome + "/tester/data_Test_File_0/file_0";
            String file_1 = repoHome + "/tester/data_Test_File_0/file_1";
            String file_2 = repoHome + "/tester/data_Test_File_0/file_2";
            String file_3 = repoHome + "/tester/data_Test_File_0/file_3";
            String missing_file_0 = repoHome + "/tester/data_Test_File_0/missing_file_0";

            // Setting modification times
            Files.setLastModifiedTime(Path.of(file_3), FileTime.fromMillis(System.currentTimeMillis() - 20000));
            Files.setLastModifiedTime(Path.of(file_2), FileTime.fromMillis(System.currentTimeMillis() - 15000));
            Files.setLastModifiedTime(Path.of(file_1), FileTime.fromMillis(System.currentTimeMillis() - 10000));
            Files.setLastModifiedTime(Path.of(file_0), FileTime.fromMillis(System.currentTimeMillis() - 5000));

            conditions[i++] = Ariadne_File.newer_than_all(file_0, List.of(file_1, file_2, file_3));

            // Updating times and repeating checks
            Files.setLastModifiedTime(Path.of(file_2), FileTime.fromMillis(System.currentTimeMillis() + 10000));
            conditions[i++] = !Ariadne_File.newer_than_all(file_0, List.of(file_1, file_2, file_3));

            Files.setLastModifiedTime(Path.of(file_1), FileTime.fromMillis(System.currentTimeMillis() + 15000));
            conditions[i++] = !Ariadne_File.newer_than_all(file_0, List.of(file_1, file_2, file_3));

            conditions[i++] = !Ariadne_File.newer_than_all(missing_file_0, List.of(file_1, file_2, file_3));

            conditions[i++] = !Ariadne_File.newer_than_all(file_0, List.of(file_1, missing_file_0));

            return Mosaic_Util.all(conditions);
        }
    }

    public static void main(String[] args) {
        try {
            TestSuite suite = new Test_File_0().new TestSuite();
            int result = Mosaic_Testbench.run(suite);
            System.exit(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
