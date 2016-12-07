package test.map;

import java.util.Random;

public class LoremIpsum {

    private static final String[] ENTRIES = {
            "lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetur",
            "adipiscing",
            "elit",
            "sed",
            "do",
            "eiusmod",
            "tempor",
            "incididunt",
            "ut",
            "labore",
            "et",
            "magna",
            "aliqua",
            "enim",
            "ad",
            "minim",
            "veniam",
            "quis",
            "nostrud",
            "exercitation",
            "ullamco",
            "laboris",
            "nisi",
            "aliquip",
            "ex",
            "ea",
            "commodo",
            "consequat",
            "duis",
            "aute",
            "irure",
            "reprehenderit",
            "in",
            "voluptate",
            "velit",
            "esse",
            "cillum",
            "dolore",
            "eu",
            "fugiat",
            "nulla",
            "pariatur",
            "excepteur",
            "sint",
            "occaecat",
            "cupidatat",
            "non",
            "proident",
            "sunt",
            "culpa",
            "qui",
            "officia",
            "deserunt",
            "mollit",
            "anim",
            "id",
            "est",
            "laborum"
    };
    public static final String SPACE_CHAR = " ";
    public static final String DOT_CHAR = ".";
    public static final String COMMA_CHAR = ",";
    public static final String PARAGRAPH_SEPARATOR;

    static {
        String s = System.getProperty("line.separator");
        PARAGRAPH_SEPARATOR = s + s;
    }

    private Random mRandom;
    private int mWordCount = 0;
    private int mMinSentenceLength = 5;
    private boolean mCapitilize = true;
    private boolean mUseSentences = true;
    private boolean mUseCommas = true;
    private boolean mMakeParagraphs = false;

    public LoremIpsum() {
        mRandom = new Random();
    }

    public LoremIpsum setWordCount(int count) {
        mWordCount = count;
        return this;
    }

    public LoremIpsum setWordCount(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be < max!");
        }
        mWordCount = min + mRandom.nextInt(max - min);
        return this;
    }

    public LoremIpsum setMinSentenceLength(int minLength) {
        mMinSentenceLength = minLength;
        return this;
    }

    public LoremIpsum setCapitilize(boolean capitilize) {
        mCapitilize = capitilize;
        return this;
    }

    public LoremIpsum useSentences(boolean useSentences) {
        mUseSentences = useSentences;
        return this;
    }

    public LoremIpsum useCommas(boolean useCommas) {
        mUseCommas = useCommas;
        return this;
    }

    public LoremIpsum makeParagraphs(boolean makeParagraphs) {
        mMakeParagraphs = makeParagraphs;
        return this;
    }

    public String build() {
        if (mWordCount <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean capitalize = mCapitilize;
        boolean nextSentence;
        boolean newLine = true;
        int wordCount = 0;
        for (int i = 0; i < mWordCount; i++) {
            String value = ENTRIES[mRandom.nextInt(ENTRIES.length)];
            if (!newLine) {
                result.append(SPACE_CHAR);
            } else {
                newLine = false;
            }
            if (capitalize) {
                value = value.substring(0, 1).toUpperCase() + value.substring(1);
                capitalize = false;
            }
            result.append(value);
            nextSentence = mUseSentences && (++wordCount >= mMinSentenceLength) &&
                    (mRandom.nextInt(3) == 0);
            boolean lastWord = (i == mWordCount - 1);
            if (lastWord || nextSentence) {
                if (mUseSentences) {
                    result.append(DOT_CHAR);
                    capitalize = mCapitilize;
                    wordCount = 0;
                }
                if (mMakeParagraphs && !lastWord && mRandom.nextInt(4) == 0) {
                    result.append(PARAGRAPH_SEPARATOR);
                    newLine = true;
                }
            } else if (mUseCommas && mRandom.nextInt(15) == 0) {
                result.append(COMMA_CHAR);
            }
        }

        return result.toString();
    }

    public String[] buildArray(int length) {
        String[] results = new String[length];
        for (int i = 0; i < length; i++) {
            results[i] = build();
        }
        return results;
    }

}
