import java.lang.reflect.Method;

public class Test_File_0 {

    public class TestSuite {

        public Boolean unpack_file_path_0(In.MIO io) {
            Boolean[] conditions = new Boolean[5];
            int i = 0;

            String test_fp = "/home/user/test.txt";
            String expected_dp = "/home/user/";
            String expected_fn = "test.txt";
            String expected_fn_base = "test";
            String expected_fn_ext = "txt";

            try {
                // Use reflection to call In.File.unpack_file_path
                Method unpackFilePath = In.File.getMethod("unpack_file_path", String.class);
                java.util.Map<String, String> result = (java.util.Map<String, String>) unpackFilePath.invoke(null, test_fp);

                conditions[i++] = result.get("dp").equals(expected_dp);
                conditions[i++] = result.get("fn").equals(expected_fn);
                conditions[i++] = result.get("fn_base").equals(expected_fn_base);
                conditions[i++] = result.get("fn_ext").equals(expected_fn_ext);
                conditions[i++] = result.size() == 4;

                Method allMethod = In.MU.getMethod("all", Boolean[].class);
                return (Boolean) allMethod.invoke(null, (Object) conditions);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public Boolean file_exists_q_0(In.MIO io) {
            Boolean[] conditions = new Boolean[2];
            int i = 0;

            String repoHome = System.getenv("REPO_HOME");
            String existingFilePath = repoHome + "/tester/data_Test_File_0/I_exist";
            String nonExistentFilePath = repoHome + "/tester/data_Test_File_0/I_do_not_exist";

            try {
                // Use reflection to call In.File.file_exists_q
                Method fileExistsMethod = In.File.getMethod("file_exists_q", String.class);
                conditions[i++] = (Boolean) fileExistsMethod.invoke(null, existingFilePath);
                conditions[i++] = !(Boolean) fileExistsMethod.invoke(null, nonExistentFilePath);

                Method allMethod = In.MU.getMethod("all", Boolean[].class);
                return (Boolean) allMethod.invoke(null, (Object) conditions);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public Boolean newer_than_all_0(In.MIO io) throws java.io.IOException {
            Boolean[] conditions = new Boolean[5];
            int i = 0;

            String repoHome = System.getenv("REPO_HOME");
            String file_0 = repoHome + "/tester/data_Test_File_0/file_0";
            String file_1 = repoHome + "/tester/data_Test_File_0/file_1";
            String file_2 = repoHome + "/tester/data_Test_File_0/file_2";
            String file_3 = repoHome + "/tester/data_Test_File_0/file_3";
            String missing_file_0 = repoHome + "/tester/data_Test_File_0/missing_file_0";

            try {
                Method setLastModifiedTime = In.Files.getMethod("setLastModifiedTime", java.nio.file.Path.class, java.nio.file.attribute.FileTime.class);
                Method getPath = In.Paths.getMethod("get", String.class);
                Method fromMillis = In.FileTime.getMethod("fromMillis", long.class);

                // Setting modification times
                setLastModifiedTime.invoke(null, getPath.invoke(null, file_3), fromMillis.invoke(null, System.currentTimeMillis() - 20000));
                setLastModifiedTime.invoke(null, getPath.invoke(null, file_2), fromMillis.invoke(null, System.currentTimeMillis() - 15000));
                setLastModifiedTime.invoke(null, getPath.invoke(null, file_1), fromMillis.invoke(null, System.currentTimeMillis() - 10000));
                setLastModifiedTime.invoke(null, getPath.invoke(null, file_0), fromMillis.invoke(null, System.currentTimeMillis() - 5000));

                Method newerThanAll = In.File.getMethod("newer_than_all", String.class, java.util.List.class);
                conditions[i++] = (Boolean) newerThanAll.invoke(null, file_0, java.util.List.of(file_1, file_2, file_3));

                // Updating times and repeating checks
                setLastModifiedTime.invoke(null, getPath.invoke(null, file_2), fromMillis.invoke(null, System.currentTimeMillis() + 10000));
                conditions[i++] = !(Boolean) newerThanAll.invoke(null, file_0, java.util.List.of(file_1, file_2, file_3));

                setLastModifiedTime.invoke(null, getPath.invoke(null, file_1), fromMillis.invoke(null, System.currentTimeMillis() + 15000));
                conditions[i++] = !(Boolean) newerThanAll.invoke(null, file_0, java.util.List.of(file_1, file_2, file_3));

                conditions[i++] = !(Boolean) newerThanAll.invoke(null, missing_file_0, java.util.List.of(file_1, file_2, file_3));

                conditions[i++] = !(Boolean) newerThanAll.invoke(null, file_0, java.util.List.of(file_1, missing_file_0));

                Method allMethod = In.MU.getMethod("all", Boolean[].class);
                return (Boolean) allMethod.invoke(null, (Object) conditions);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void main(String[] args) {
        try {
            Method runMethod = In.TB.getMethod("run", Object.class);
            TestSuite suite = new Test_File_0().new TestSuite();
            int result = (int) runMethod.invoke(null, suite);
            System.exit(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
