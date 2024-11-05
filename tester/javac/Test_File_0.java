
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;

import com.ReasoningTechnology.Ariadne.File;
import com.ReasoningTechnology.Mosaic.*;

public class Test_File_0{

  public class TestSuite{

    public Boolean unpack_file_path_0(IO io){
      Boolean[] conditions = new Boolean[5];
      int i = 0;
        
      // Test input
      String test_fp = "/home/user/test.txt";
        
      // Expected output
      String expected_dp = "/home/user/";
      String expected_fn = "test.txt";
      String expected_fn_base = "test";
      String expected_fn_ext = "txt";

      // Actual output
      Map<String, String> result = File.unpack_file_path( test_fp );
        
      conditions[i++] = result.get("dp").equals( expected_dp );
      conditions[i++] = result.get("fn").equals( expected_fn );
      conditions[i++] = result.get("fn_base").equals( expected_fn_base );
      conditions[i++] = result.get("fn_ext").equals( expected_fn_ext );
      conditions[i++] = result.size() == 4;

      // Return true if all conditions are met
      return MU.all( conditions );
    }

    public Boolean file_exists_q_0(IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test file paths, assuming $REPO_HOME is set in the environment
      String repoHome = System.getenv("REPO_HOME");
      String existingFilePath = repoHome + "/tester/data_Test_File_0/I_exist";
      String nonExistentFilePath = repoHome + "/tester/data_Test_File_0/I_do_not_exist";

      // Test cases
      conditions[i++] = File.file_exists_q(existingFilePath); // Expect true for existing file
      conditions[i++] = !File.file_exists_q(nonExistentFilePath); // Expect false for non-existent file

      // Return true if both conditions are met
      return MU.all(conditions);
    }

    public Boolean newer_than_all_0(IO io) throws IOException {
      Boolean[] conditions = new Boolean[5];
      int i = 0;

      String repoHome = System.getenv("REPO_HOME");

      // Define paths for existing and missing files
      String file_0 = repoHome + "/tester/data_Test_File_0/file_0";
      String file_1 = repoHome + "/tester/data_Test_File_0/file_1";
      String file_2 = repoHome + "/tester/data_Test_File_0/file_2";
      String file_3 = repoHome + "/tester/data_Test_File_0/file_3";
      String missing_file_0 = repoHome + "/tester/data_Test_File_0/missing_file_0";
    
      // Set modification times: file_0 is the youngest, file_3 is the oldest
      Files.setLastModifiedTime(Paths.get(file_3), FileTime.fromMillis(System.currentTimeMillis() - 20000));
      Files.setLastModifiedTime(Paths.get(file_2), FileTime.fromMillis(System.currentTimeMillis() - 15000));
      Files.setLastModifiedTime(Paths.get(file_1), FileTime.fromMillis(System.currentTimeMillis() - 10000));
      Files.setLastModifiedTime(Paths.get(file_0), FileTime.fromMillis(System.currentTimeMillis() - 5000));

      // Test case 1: file_0 is newer than all other files
      conditions[i++] = File.newer_than_all(file_0, List.of(file_1, file_2, file_3)); // Expect true

      // Test case 2: file_0 is newer than some, but not all (make file_2 newer)
      Files.setLastModifiedTime(Paths.get(file_2), FileTime.fromMillis(System.currentTimeMillis() + 10000)); // file_2 is now newer
      conditions[i++] = !File.newer_than_all(file_0, List.of(file_1, file_2, file_3)); // Expect false

      // Test case 3: file_0 is not newer than any (make both file_1 and file_2 newer)
      Files.setLastModifiedTime(Paths.get(file_1), FileTime.fromMillis(System.currentTimeMillis() + 15000));
      conditions[i++] = !File.newer_than_all(file_0, List.of(file_1, file_2, file_3)); // Expect false

      // Test case 4: file_0 does not exist
      conditions[i++] = !File.newer_than_all(missing_file_0, List.of(file_1, file_2, file_3)); // Expect false

      // Test case 5: Some files in the list are missing
      conditions[i++] = !File.newer_than_all(file_0, List.of(file_1, missing_file_0)); // Expect false

      // Return true if all conditions pass
      return MU.all(conditions);
    }

  }

  public static void main(String[] args) {
    TestSuite suite = new Test_File_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}

