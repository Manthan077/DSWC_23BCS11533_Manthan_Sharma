public class DNASequencerOptimization {

    static class DNASequencer {

        private StringBuilder dnaSequence =
                new StringBuilder(100000);

        public void ingestSequence(
                char[] sensorData) {

            for (char c : sensorData) {

                dnaSequence.append(c);
            }
        }

        public void mutateDNA(
                String target,
                String replacement) {

            int index =
                    dnaSequence.indexOf(target);

            if (index != -1) {

                dnaSequence.replace(
                        index,
                        index + target.length(),
                        replacement
                );
            }
        }

        public String getSequence() {

            return dnaSequence.toString();
        }
    }

    public static void main(String[] args) {

        DNASequencer dna =
                new DNASequencer();

        dna.ingestSequence(
                new char[]{
                        'A','T','G',
                        'C','A','A'
                });

        dna.mutateDNA(
                "TG",
                "CC");

        System.out.println(
                dna.getSequence());
    }
}