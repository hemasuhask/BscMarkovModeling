import java.util.*;


public class HashMarkov implements MarkovInterface {


    protected String[] myWords;		
	protected Random myRandom;		
	protected int myOrder;			
	protected static String END_OF_TEXT = "*** ERROR ***"; 
    HashMap<WordGram, List<String>> myMap = new HashMap<>();
    


    public HashMarkov() {
		this(3);
	}

	public HashMarkov(int order){
		myOrder = order;
		myRandom = new Random();
	}


    @Override
    public void setTraining(String text) {
        myMap.clear();
        myWords = text.split("\\s+");

        for (int i = 0; i < myWords.length - myOrder; i++) {
            WordGram wg = new WordGram(myWords, i, myOrder);
            String nextWord = myWords[i + myOrder];

            myMap.computeIfAbsent(wg, k -> new ArrayList<>()).add(nextWord);
        }
    }



    @Override
    public List<String> getFollows(WordGram wgram) {
        return myMap.getOrDefault(wgram, new ArrayList<>());
    }



    @Override
    public String getRandomText(int length) {
        ArrayList<String> randomWords = new ArrayList<>(length);
        int index = myRandom.nextInt(myWords.length - myOrder + 1);
        WordGram current = new WordGram(myWords, index, myOrder);
        randomWords.add(current.toString());

        for (int k = 0; k < length - myOrder; k++) {
            String nextWord = getNextWord(current);
            if (nextWord.equals(BaseMarkov.END_OF_TEXT)) {
                break;
            }
            randomWords.add(nextWord);
            current = current.shiftAdd(nextWord);
        }
        return String.join(" ", randomWords);
    }

    private String getNextWord(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows.size() == 0) {
			return END_OF_TEXT;
		}
		else {
			int randomIndex = myRandom.nextInt(follows.size());
			return follows.get(randomIndex);
		}
	}



    @Override
    public int getOrder() {
        return myOrder;
    }


    @Override
    public void setSeed(long seed) {
        myRandom.setSeed(seed);
    }
    
}
