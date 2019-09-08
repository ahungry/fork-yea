// need to ensure clojure jar is on CLASSPATH
import clojure.lang.IFn;
import clojure.java.api.Clojure;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HelloJNI {
  static {
    System.loadLibrary("hello"); // Load native library hello.dll (Windows) or libhello.so (Unixes)
    //  at runtime
    // This library contains a native method called sayHello()
  }

  // Declare an instance native method sayHello() which receives no parameter and returns void
  private native void sayHello();
  private native int fork();
  private native int getPid();
  private native String addOne(int y);

  // Test Driver
  public static void main(String[] args) {
    new HelloJNI().sayHello();  // Create an instance and invoke the native method

    // What will happen??
    int pid = new HelloJNI().fork();
    String answer;

    if (0 == pid)
      {
        answer = new HelloJNI().addOne(2);
      }
    else
      {
        answer = new HelloJNI().addOne(5);
      }

    System.out.println(answer);
    System.out.println("All done?");
  }

  public static void halt(int pid) {
    try {
      Process p = Runtime.getRuntime().exec("kill -9 " + pid);
      Runtime.getRuntime().halt(0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void writeOut (String fileName, String result)
    throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    System.out.println("About to write out the data.");
    writer.write(result);
    writer.close();
  }

  public static int forkYea(IFn f) {
    new HelloJNI().sayHello();  // Create an instance and invoke the native method

    // What will happen??
    int pid = new HelloJNI().fork();
    String answer;

    // Child, do what we want (eval something?)
    if (0 == pid)
      {
        // answer = new HelloJNI().addOne(2);
        System.out.println("Child about to invoke...");
        // System.out.println(f.invoke());
        String result = f.invoke().toString();
        System.out.println("Done invoking the child" + result);
        int cpid = new HelloJNI().getPid();
        // String fileName = "/tmp/" + String.valueOf(cpid);
        String fileName = String.valueOf(cpid + ".forkyea");
        System.out.println("Make a file of this name: " + fileName);

        // TODO: This means each call will require a single I/O
        // We will want to change this to be a non disk bound later.
        try {
          HelloJNI.writeOut(fileName, result);
        } catch (IOException ex) {
          ex.printStackTrace();
        }

        // BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        // writer.write(result);
        // writer.close();

        // System.out.println("Child invoked...");
        // This is gonna be killed from parent shortly...
        // HelloJNI.halt();

        // System.out.println("Child pid was: " + new HelloJNI().getPid());
        HelloJNI.halt(cpid);
      }
    return pid;

    // else
    //   {
    //     // For now, we have no reason to invoke as parent....
    //     // answer = new HelloJNI().addOne(5);
    //     // System.out.println("Parent about to invoke...");
    //     // System.out.println(f.invoke());
    //     // System.out.println("Parent invoked...");
    //     // System.out.println("Parent sees child pid was: " + pid);
    //     // HelloJNI.halt(pid);
    //   }

    // System.out.println(answer);
    // System.out.println("All done?");

    // return pid;
  }
}

// It actually works and presents the following output:
// Will it work wth Clojure?....

// Hello World!
// Fork time!I am the parent!
// Fork time!I am the child!
// 6
// All done?
// 3
// All done?
