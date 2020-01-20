import java.io.*;
import java.util.*;

public class LogCounter {

  // public static class MaxPQ<Entry extends Comparable<entry>>{
  //   int size;
  //   int rank;//keep number
  //
  //   MaxPQ(int rank) {
  //     this.size=0;
  //     this.rank=rank;
  //   }
  //
  //   public void add(Entry e) {
  //
  //   }
  //
  //   public int getSize() { return size;}
  //   public boolean isEmpty() { return size==0;}
  // }

  // private static class TagComparator implements Comparator<Map.Entry<String,Integer>> {
  //   @Override
  //   public int compare(Map.Entry<String,Integer> a, Map.Entry<String,Integer> b) {
  //     return a.getValue() - b.getValue();
  //   }
  // }

  public static void main(String[] args) {
    if (args.length != 2 || args[0].equals("-h")) {
      System.out.println("usage: java LogCounter [tagindex] [maxrank] < [logfile]");
      return;
    }

    final int index = Integer.parseInt(args[0]);
    final int rank = Integer.parseInt(args[1]);
    BufferedReader br=null;
    int lines = 0;
    int skip = 0;

    try {
      br = new BufferedReader(new InputStreamReader(System.in));
      Map<String,Integer> map = new HashMap<>();

      //count tag
      for (String s = br.readLine();s != null;s=br.readLine(),lines++) {
        String[] sa = s.split("\\s+");
        if (index < sa.length) {
          String tag = sa[index];
          // System.out.println(tag);
          if (map.containsKey(tag)) {
            map.put(tag,map.get(tag)+1);
          } else {
            map.put(tag,1);
          }
        } else {
          skip++;
          System.out.println("skip line " + lines + ":" + s);
        }
      }

      //sort
      // MaxPQ pq = new MaxPQ<>(10);
      Queue<Map.Entry<String,Integer>> pq = new PriorityQueue<>(8,new Comparator<Map.Entry<String,Integer>>() {
        @Override
        public int compare(Map.Entry<String,Integer> a, Map.Entry<String,Integer> b) {
          //b-a lead desc-order
          return b.getValue() - a.getValue();
        }
      });
      pq.addAll(map.entrySet());

      //output
      System.out.println("=== SAMMARY ===");
      System.out.printf("total\t %d\t lines\n", lines);
      System.out.printf("skip\t %d\t lines\n", skip);
      System.out.printf("total\t %d\t tags\n", map.entrySet().size());
      System.out.println("=== COUNT ===");
      System.out.printf("%40s %9s %5s\n", "TAG","COUNT","PER");
      for (int i=0; i<rank && !pq.isEmpty();i++){
        Map.Entry<String,Integer> e = pq.remove();
        System.out.printf("%40s  %9d  %5d%%\n",e.getKey(),e.getValue(),e.getValue()*100/lines);
      }
      // List<Map.Entry<String,Integer>> l = new ArrayList<Map.Entry<String,Integer>>(map.entrySet())

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
