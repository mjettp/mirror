package mirror;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class NativeFileAccess implements FileAccess {

  public static void main(String[] args) throws Exception {
    NativeFileAccess f = new NativeFileAccess();
    Path bar = Paths.get("/home/stephen/dir1/bar.txt");
    ByteBuffer b = f.read(bar);
    String s = Charsets.US_ASCII.newDecoder().decode(b).toString();
    System.out.println(s);
    f.write(bar, ByteBuffer.wrap((s + "2").getBytes()));
  }

  @Override
  public void write(Path path, ByteBuffer data) throws IOException {
    FileChannel c = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    try {
      c.write(data);
    } finally {
      c.close();
    }
  }

  @Override
  public ByteBuffer read(Path path) throws IOException {
    return Files.map(path.toFile());
  }

  @Override
  public long getModifiedTime(Path path) {
    return path.toFile().lastModified();
  }

  @Override
  public void delete(Path path) throws IOException {
    path.toFile().delete();
  }

}
