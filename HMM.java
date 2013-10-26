
public class HMM {

	private DoubleMatrix pi;
	private DoubleMatrix A;
	private DoubleMatrix B;

	private int number_of_states;
	private int number_of_emissions; // possible emissions.

	public HMM(DoubleMatrix pi, DoubleMatrix A, DoubleMatrix B) {
		this.setPi(pi);
		this.setA(A);
		this.setB(B);

		this.number_of_states = A.height;
		this.number_of_emissions = B.width;
	}

	public DoubleMatrix getPi() {
		return pi;
	}

	public void setPi(DoubleMatrix pi) {
		this.pi = pi;
	}

	public DoubleMatrix getA() {
		return A;
	}

	public void setA(DoubleMatrix a) {
		A = a;
	}

	public DoubleMatrix getB() {
		return B;
	}

	public void setB(DoubleMatrix b) {
		B = b;
	}

	public int getNumberOfStates() {
		return number_of_states;
	}

	public int getNumberOfEmissions() {
		return number_of_emissions;
	}

	private double[] alpha_scaling;

	public double[] alphaScales() {
		return alpha_scaling;
	}

	public double logProb() {
		double sum = 0;
		for (double ct : alpha_scaling)
			sum += Math.log(ct);
		return -sum;
	}

	/***
	 * Probability of emission sequence given initial state matrix.
	 * 
	 * @param emissions
	 * @param transitionMatrix
	 * @param emissionMatrix
	 * @param initialState
	 * @return
	 */
	public DoubleMatrix alpha(int[] emissions) {
		// if(alpha != null)
		// return alpha;
		//
		alpha_scaling = new double[emissions.length];

		// int emission = emissions[e];
		DoubleMatrix alpha = new DoubleMatrix(number_of_states,
				emissions.length);
		alpha_scaling[0] = 0;
		for (int i = 0; i < number_of_states; ++i) {
			double val = pi.get(0, i) * B.get(i, emissions[0]);
			alpha.set(0, i, val);
			alpha_scaling[0] += val;
		}

		// scale the α0(i)
		// if(alpha_scaling[0] != 0)
		alpha_scaling[0] = 1 / alpha_scaling[0];

		for (int i = 0; i < number_of_states; ++i) {
			double val = alpha.get(0, i) * alpha_scaling[0];
			alpha.set(0, i, val);
		}

		for (int t = 1; t < alpha.height; ++t) {

			alpha_scaling[t] = 0;
			for (int i = 0; i < number_of_states; ++i) {

				double val = 0;
				for (int j = 0; j < number_of_states; j++) {
					val += A.get(j, i) * alpha.get(t - 1, j);
				}

				alpha.set(t, i, val * B.get(i, emissions[t]));
				alpha_scaling[t] += alpha.get(t, i);
			}

			// scale the αt(i)
			// if(alpha_scaling[t] != 0)
			alpha_scaling[t] = 1 / alpha_scaling[t];
			for (int i = 0; i < number_of_states; ++i)
				alpha.set(t, i, alpha.get(t, i) * alpha_scaling[t]);
		}

		// System.err.println("Alpha:");
		// System.err.println(alpha);

		return alpha;
	}

	/**
	 * 
	 * @param emissions
	 * @param transitionMatrix
	 * @param emissionMatrix
	 * @param initialState
	 * @return
	 */
	public DoubleMatrix beta(int[] emissions) {
		// if(beta != null)
		// return beta;

		DoubleMatrix beta = new DoubleMatrix(number_of_states, emissions.length);
		for (int i = 0; i < number_of_states; ++i) {
			beta.set(emissions.length - 1, i,
					alpha_scaling[emissions.length - 1]);
		}

		for (int t = emissions.length - 2; t > -1; --t) {

			for (int i = 0; i < number_of_states; ++i) {
				double val = 0;
				for (int j = 0; j < number_of_states; ++j) {
					val += A.get(i, j) * B.get(j, emissions[t + 1])
							* beta.get(t + 1, j);
				}

				// Scaling here?
				beta.set(t, i, val * alpha_scaling[t]);
			}
		}
		//
		// System.err.println("Beta:");
		// System.err.println(beta);
		//
		return beta;
	}

	/**
	 * Compute the most probable sequence of states given a sequence of
	 * emissions.
	 * 
	 * @param emissions
	 * @return
	 */
	public int[] viterbi(int[] emissions) {

		DoubleMatrix t1 = new DoubleMatrix(emissions.length, A.height);
		DoubleMatrix t2 = new DoubleMatrix(emissions.length, A.height);

		int timesteps = emissions.length; // for easier reading..

		for (int s = 0; s < A.height; ++s) {
			t1.set(s, 0,
					(Math.log(pi.get(0, s)) + Math.log(B.get(s, emissions[0]))));
			// t1.set(s, 0, pi.get(0,s) * B.get(s, emissions[0]));
			t2.set(s, 0, 0.0d);
		}

		// System.err.println(t1);
		// System.err.println(t2);

		for (int t = 1; t < timesteps; ++t) {
			for (int s = 0; s < number_of_states; ++s) {
				double t1max = Double.NEGATIVE_INFINITY;
				double t2max = -1;

				for (int k = 0; k < A.height; ++k) {
					double v1 = Math.log(B.get(s, emissions[t])); // Probability
																	// of
																	// getting
																	// the
																	// emission
																	// in this
																	// state.
					v1 += Math.log(A.get(k, s)); // Probability previous state k
													// and of
					v1 += t1.get(k, t - 1);
					// double v1 = B.get(s, emissions[t]); // Probability of
					// getting the emission in this state.
					// v1 *= A.get(k, s); // Probability previous state k and of
					// v1 *= t1.get(k, t-1);
					if (v1 > t1max) {
						t1max = v1;
						t2max = k;
					}
				}

				t1.set(s, t, t1max);
				t2.set(s, t, t2max);
			}
		}

		// System.err.println(t1);
		// System.err.println(t2);

		int[] z = new int[timesteps];
		int[] x = new int[timesteps]; // states

		double maxval = Double.NEGATIVE_INFINITY;
		for (int s = 0; s < number_of_states; ++s) {
			double prob = t1.get(s, timesteps - 1);
			if (maxval < prob) {
				maxval = prob;
				z[timesteps - 1] = (int) t2.get(s, timesteps - 1); // points
																	// back to
																	// old
																	// step.'
				x[timesteps - 1] = s;
			}
		}

		for (int t = timesteps - 2; t > -1; --t) {
			if (z[t + 1] == -1) {
				// System.err.println(A);
				// System.err.println(B);
				// System.err.println(t2);
				// throw new RuntimeErrorException(new Error("derp"));
				return null;
			}

			z[t] = (int) t2.get(z[t + 1], t);
			x[t] = z[t + 1];
		}

		return x;
	}

	public String toKattisString() {
		StringBuilder builder = new StringBuilder();

		builder.append(pi.toOutput(true)).append("\n");
		builder.append(A.toOutput(true)).append("\n");
		builder.append(B.toOutput(true)).append("\n");

		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("pi\n");
		builder.append(pi);
		builder.append("A\n");
		builder.append(A);
		builder.append("B\n");
		builder.append(B);

		return builder.toString();
	}

	public static HMM parseFromString(String str) {
		String[] rows = str.split("\n");

		DoubleMatrix pi = DoubleMatrix.parseFromRow(rows[0]);
		DoubleMatrix A = DoubleMatrix.parseFromRow(rows[1]);
		DoubleMatrix B = DoubleMatrix.parseFromRow(rows[2]);

		return new HMM(pi, A, B);
	}
}
