package prodcons;

class Producer implements Runnable {
  // needs access to a *shared* BlockingQueue
  // create a series of data items--array of two int
  // for a range of indexes (e.g. 0-9999)
    // create the new array
    // "in steps" (simulating passing through a "transactionally unsafe" point
    //   set the data to { index, index }
    // put that item into the queue (what do we do after put?)
  // print "finished"

  // items should { 0, 0 } then { 1, 1 }
  // create (locally) {-1, 0} first...
  // then change it to {0, 0}, then put it.
  // and proceed with the incremeting index...
}

class Consumer implements Runnable {
  // shared BlockingQueue
  // for the *same* range of indexes..
    // read the next item from the queue
    // validate it -- two subscripts should be equal to the index
    // (how could you verify this test works)
  // print finished
}

public class LabExercise {
  // configure the above, with an actual ArrayBlockingQueue (length 10 is fine)
  // kick it off
  // let it run to completion
  // satisfy yourself that:
  // transactional issues are solid
  // timing issues are solid (can you prove that queue was sometimes
  //   full, sometimes empty, yet still worked properly
  // visibility is solid (look at documentation)
  // you understand it :)

  // Extra credit (hard)
  // how could you modify this idea to have multiple producers
  // multiple consumers, and still validate that all the right
  // right data was received
}
