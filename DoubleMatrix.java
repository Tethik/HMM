
public class DoubleMatrix {

	public int width;
	public int height;

	private double[][] matrix;

	public DoubleMatrix(int width, int height) {
		this.width = width;
		this.height = height;
		this.matrix = new double[height][width];
	}

	public double get(int row, int col) {
		if (col >= width)
			throw new ArrayIndexOutOfBoundsException("X > " + width);
		if (row >= height)
			throw new ArrayIndexOutOfBoundsException("Y > " + height);

		return this.matrix[row][col];
	}

	public DoubleMatrix getColumn(int c) {
		DoubleMatrix column = new DoubleMatrix(1, height);
		for (int i = 0; i < height; i++) {
			column.set(i, 0, get(i, c));
		}
		return column;
	}

	public DoubleMatrix getRow(int r) {
		DoubleMatrix row = new DoubleMatrix(width, 1);
		for (int i = 0; i < height; i++) {
			row.set(0, i, get(r, i));
		}
		return row;
	}

	public void set(int row, int col, double val) {
		this.matrix[row][col] = val;
	}

	/**
	 * Parse DoubleMatrix from KATTIS-formatted string.
	 * 
	 * @param row
	 * @return
	 */
	public static DoubleMatrix parseFromRow(String row) {
		String[] words = row.split(" ");

		int height = Integer.parseInt(words[0]);
		int width = Integer.parseInt(words[1]);

		DoubleMatrix matrix = new DoubleMatrix(width, height);
		for (int i = 2; i < words.length; i++) {
			double val = Double.parseDouble(words[i]);
			matrix.set((i - 2) / width, (i - 2) % width, val);
		}
		return matrix;
	}

	public static DoubleMatrix identity(int size) {
		DoubleMatrix id = new DoubleMatrix(size, size);

		for (int i = 0; i < size; ++i) {
			id.set(i, i, 1.0d);
		}

		return id;
	}

	public DoubleMatrix add(DoubleMatrix m2) {
		if (this.height != m2.height || this.width != m2.width)
			throw new IllegalArgumentException("Differing width or height");

		DoubleMatrix mNew = new DoubleMatrix(this.width, this.height);

		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < width; ++col) {
				mNew.set(row, col, get(row, col) + m2.get(row, col));
			}
		}

		return mNew;
	}

	/**
	 * 
	 * @param toMultiplyWith
	 * @return
	 */
	public DoubleMatrix dotProduct(DoubleMatrix toMultiplyWith) {

		DoubleMatrix product = new DoubleMatrix(toMultiplyWith.width, height);

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < toMultiplyWith.width; ++x) {
				double prod = 0;
				for (int i = 0; i < width; ++i) {
					prod += this.get(y, i) * toMultiplyWith.get(i, x);
				}

				product.set(y, x, prod);
			}
		}

		return product;
	}

	public DoubleMatrix scalarProduct(double scalar) {
		DoubleMatrix clone = new DoubleMatrix(width, height);

		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				clone.set(y, x, scalar * get(y, x));

		return clone;
	}
	
	public DoubleMatrix scalarDivide(double scalar) {
		DoubleMatrix clone = new DoubleMatrix(width, height);

		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				clone.set(y, x, (scalar == 0) ? 0 : (get(y, x) / scalar));

		return clone;
	}

	public DoubleMatrix addScalar(double scalar) {
		DoubleMatrix clone = new DoubleMatrix(width, height);
		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				clone.set(y, x, scalar + get(y, x));

		return clone;
	}

	public double difference(DoubleMatrix matrix) {
		double diff = 0.0d;
		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < width; ++col) {
				diff += Math.abs(get(row, col) - matrix.get(row, col));
			}
		}
		return diff;
	}

	/**
	 * Gets a string representation of this Matrix.
	 * 
	 * @param kattisOutput
	 *            enable to return the matrix in a single line of text.
	 * @return
	 */
	public String toOutput(boolean kattisOutput) {
		StringBuilder builder = new StringBuilder();
		if (kattisOutput)
			builder.append(height).append(" ").append(width).append(" ");

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				builder.append(this.matrix[y][x]).append(" ");
			}
			if (!kattisOutput)
				builder.append("\n");
		}
		return builder.toString();

	}

	@Override
	public String toString() {
		return toOutput(false);
	}
	
	public DoubleMatrix scaleRows() {
		DoubleMatrix clone = new DoubleMatrix(width, height);
		for (int y = 0; y < height; ++y) {
			double sum = 0;
			for (int x = 0; x < width; ++x)
				sum += get(y,x);
			for (int x = 0; x < width; ++x)
				clone.set(y, x, (sum == 0) ? 0 : (get(y, x) / sum));
		}
		return clone;
	}

	// public static DoubleMatrix randomByRowSum(int width, int height, double
	// sumvalue) {
	// Random random = new Random();
	// DoubleMatrix toRet = new DoubleMatrix(width, height);
	//
	// for(int i = 0; i < height; ++i)
	// {
	// double sum = sumvalue;
	// for(int j = 0; j < width; ++j)
	// {
	// double val = sum;
	// if(j < width - 1)
	// val = random.nextDouble() * sum;
	//
	// toRet.set(i, j, val);
	// sum -= val;
	// }
	// }
	//
	// return toRet;
	// }

	public double rowSum(int row) {
		double sum = 0;
		for (int c = 0; c < width; ++c)
			sum += get(row, c);
		return sum;
	}

}
