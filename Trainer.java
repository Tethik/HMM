
public class Trainer {

	private HMM initial;
	private int max_iterations = 2000;

	public Trainer(HMM initial) {
		this.initial = initial;
	}

	public HMM getInitial() {
		return initial;
	}

	public int getMaxIterations() {
		return max_iterations;
	}

	public void setMaxIterations(int max_iterations) {
		this.max_iterations = max_iterations;
	}

	public HMM TrainModel(int[] emissions) {
		return TrainModel(this.initial, emissions);
	}

	private DoubleMatrix estimatedInitialState(int[] emissions) {
		DoubleMatrix newPi = new DoubleMatrix(initial.getPi().width,
				initial.getPi().height);

		for (int i = 0; i < initial.getNumberOfStates(); ++i) {
			newPi.set(0, i, gamma.get(0, i));
		}

		return newPi;
	}

	private DoubleMatrix estimatedTransitionMatrix(int[] emissions) {

		DoubleMatrix estTransMatrix = new DoubleMatrix(
				initial.getNumberOfStates(), initial.getNumberOfStates());

		for (int i = 0; i < initial.getNumberOfStates(); ++i) {
			for (int j = 0; j < initial.getNumberOfStates(); ++j) {
				double numer = 0;
				double denom = 0;

				for (int t = 0; t < emissions.length - 1; ++t) {
					numer += digamma[t].get(i, j);
					denom += gamma.get(t, i);
				}

				if (numer != 0 && denom != 0)
					estTransMatrix.set(i, j, numer / denom);
			}

		}

		// System.err.println(estTransMatrix);

		return estTransMatrix;
	}

	private DoubleMatrix estimatedEmissionMatrix(int[] emissions) {
		DoubleMatrix estEmissionMatrix = new DoubleMatrix(
				initial.getNumberOfEmissions(), initial.getNumberOfStates());

		for (int i = 0; i < initial.getNumberOfStates(); ++i) {
			for (int j = 0; j < initial.getNumberOfEmissions(); ++j) {
				double numer = 0;
				double denom = 0;

				for (int t = 0; t < emissions.length - 1; ++t) {
					if (emissions[t] == j)
						numer += gamma.get(t, i);
					denom += gamma.get(t, i);
				}

				if (numer != 0 && denom != 0)
					estEmissionMatrix.set(i, j, numer / denom);
			}
		}

		// System.err.println(estEmissionMatrix);

		return estEmissionMatrix;
	}

	/**
	 * Given the sequence and current estimate of the HMM model, what is the
	 * probability that at time (t) the hidden state is (Xt=i)?
	 */
	private DoubleMatrix gamma = null;

	/**
	 * Given the sequence and current estimate of the HMM model, what is the
	 * probability that at time (t) the hidden state is (Xt=i) && at time (t+1)
	 * the hidden state is (Xt+1=j)?
	 */
	private DoubleMatrix[] digamma = null;

	/**
	 * Expresses the probability of being in state i at time t given the
	 * measurement sequence O
	 * 
	 * @return
	 */
	private void calculateDigamma(int[] emissions, HMM current) {
		digamma = new DoubleMatrix[emissions.length];
		gamma = new DoubleMatrix(initial.getNumberOfStates(), emissions.length);

		DoubleMatrix alpha = current.alpha(emissions);
		DoubleMatrix beta = current.beta(emissions);

		// t = 0 to T - 2
		for (int t = 0; t < emissions.length - 1; ++t) {
			double denom = 0;
			// i = 0 to N - 1
			for (int i = 0; i < initial.getNumberOfStates(); ++i) {
				// j = 0 to N - 1
				for (int j = 0; j < initial.getNumberOfStates(); ++j) {
					double alp = alpha.get(t, i);
					double bet = beta.get(t + 1, j);
					double tran = current.getA().get(i, j);
					double em = current.getB().get(j, emissions[t + 1]);

					denom += alp * tran * em * bet;
				}
			}

			digamma[t] = new DoubleMatrix(current.getNumberOfStates(),
					current.getNumberOfStates());

			// i = 0 to N - 1
			for (int i = 0; i < current.getNumberOfStates(); ++i) {
				double sum = 0;
				// j = 0 to N - 1
				for (int j = 0; j < current.getNumberOfStates(); ++j) {
					double alp = alpha.get(t, i);
					double bet = beta.get(t + 1, j);
					double tran = current.getA().get(i, j);
					double em = current.getB().get(j, emissions[t + 1]);

					double val = 0;
					if (denom != 0)
						val = alp * tran * em * bet / denom;
					digamma[t].set(i, j, val);
					sum += val;
				}
				gamma.set(t, i, sum);
			}

			// System.err.println("Digamma (" + t + "):");
			// System.err.println(digamma[t]);
		}

		// System.err.println("Gamma:");
		// System.err.println(gamma);
	}

	/**
	 * BaumWelch
	 * 
	 * @param previousModel
	 * @param emissions
	 * @return
	 */
	public HMM TrainModel(HMM previousModel, int[] emissions) {

		HMM current = previousModel;
		calculateDigamma(emissions, current);
		for (int i = 0; i < max_iterations; ++i) {

			// Update our model.
			double oldLogProb = current.logProb();
			DoubleMatrix ePi = estimatedInitialState(emissions);
			DoubleMatrix eA = estimatedTransitionMatrix(emissions);
			DoubleMatrix eB = estimatedEmissionMatrix(emissions);
			HMM estimate = new HMM(ePi, eA, eB);

			calculateDigamma(emissions, estimate);
			double currentLogProb = estimate.logProb();
			// System.err.println(oldLogProb + " " + currentLogProb);
			if (currentLogProb <= oldLogProb)
				break;
			current = estimate;
		}

		return current;
	}

}
