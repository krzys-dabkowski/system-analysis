/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.systempaths;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * Finds the shortest paths between nodes.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class ShortestPathFinder {

	/*int size;
	private float matrixA[][];
	private Integer matrixB[][];*/

	public ShortestPathFinder() {
		
	}
	
	private float[][] createDelayMatrix() {
		int size = ModelProvider.INSTANCE.getVariables().size();
		float [][] delayMatrix = new float [size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float val = ModelProvider.INSTANCE.getVariables().get(i).getDelayArray().get(j).getVariableValue();
				if(val == 0) {
					delayMatrix[i][j] = Float.POSITIVE_INFINITY;
				} else {
					delayMatrix[i][j] = val;
				}

			}
		}
		return delayMatrix;
	}
	
	private Integer[][] createNodeMatrix() {
		int size = ModelProvider.INSTANCE.getVariables().size();
		Integer [][] nodeMatrix = new Integer[size][size];

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float val = ModelProvider.INSTANCE.getVariables().get(i).getDelayArray().get(j).getVariableValue();
				if(val == 0) {
					nodeMatrix[i][j] = 0;
				} else {
					nodeMatrix[i][j] = null;
				}

			}
		}
		return nodeMatrix;
	}
	
	private float[][] createEdgesMatrix() {
		int size = ModelProvider.INSTANCE.getVariables().size();
		float [][] edgesMatrix = new float[size][size];

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float val = ModelProvider.INSTANCE.getVariables().get(i).getImpactArray().get(j).getVariableValue();
				if(val == 0) {
					edgesMatrix[i][j] = Float.POSITIVE_INFINITY;
				} else {
					edgesMatrix[i][j] = 1;
				}

			}
		}
		return edgesMatrix;
	}
	
	private float[][] createEffectMatrix() {
		int size = ModelProvider.INSTANCE.getVariables().size();
		float [][] effectMatrix = new float [size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float val = ModelProvider.INSTANCE.getVariables().get(i).getImpactArray().get(j).getVariableValue();
				if(val == 0) {
					effectMatrix[i][j] = Float.POSITIVE_INFINITY;
				} else {
					effectMatrix[i][j] = val;
				}

			}
		}
		return effectMatrix;
	}

	public Path[][] findPathsFW() throws Exception {
		float[][] impactMatrix = createEffectMatrix();
		float[][] delayMatrix = createDelayMatrix();
		Integer[][] nodeMatrix = createNodeMatrix();
		if(delayMatrix.length != nodeMatrix.length) {
			throw new Exception("Matrices of not the same length");
		}
		int size = delayMatrix.length;
		for(int index = 0; index < size; index++) {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(i != index && j != index) {
						// direct distance:
						float direct = delayMatrix[i][j];
						// over index:
						float indirect = delayMatrix[i][index] + delayMatrix[index][j];
						if(indirect < direct) {
							nodeMatrix[i][j] = index;
							delayMatrix[i][j] = indirect;
							impactMatrix[i][j] = impactMatrix[i][index] * impactMatrix[index][j];
						}
					}
				}
			}
		}
		Path[][] paths = new Path[delayMatrix.length][delayMatrix.length];
		for(int i = 0; i < delayMatrix.length; i++) {
			for(int j = 0; j < delayMatrix.length; j++) {
				Path path = new Path();
				path.setDelay(delayMatrix[i][j]);
				path.setEffect(impactMatrix[i][j]);
				paths[i][j] = path;
			}
		}
		return paths;
	}
	
	public float[][] findPathsAdaptedFW() throws Exception {
		float[][] effectMatrix = createEffectMatrix();
		float[][] edgesMatrix = createEdgesMatrix();
		if(effectMatrix.length != edgesMatrix.length) {
			throw new Exception("Matrices of not the same length");
		}
		int size = effectMatrix.length;
		for(int index = 0; index < size; index++) {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(i != index && j != index) {
						// direct distance:
						float directEdges = edgesMatrix[i][j];
						float directEffect = effectMatrix[i][j];
						// over index:
						float indirectEdges = edgesMatrix[i][index] + edgesMatrix[index][j];
						float indirectEffect = effectMatrix[i][index] + effectMatrix[index][j];
						if(indirectEdges < directEdges) {
							edgesMatrix[i][j] = indirectEdges;
							effectMatrix[i][j] = indirectEffect;
						} else if(indirectEdges == directEdges && indirectEffect < directEffect) {
							effectMatrix[i][j] = indirectEffect;
						}
					}
				}
			}
		}
		return effectMatrix;
	}

	public void printMatrices(float [][]matrixA, float[][] matrixB) {
		int size = matrixA.length;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float valA = matrixA[i][j];
				if(valA == Float.POSITIVE_INFINITY) {
					System.out.print("Inf ");
				} else {
					System.out.print(valA + " ");
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println();

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				float valB = matrixB[i][j];
				if(valB == Float.POSITIVE_INFINITY) {
					System.out.print("Inf ");
				} else {
					System.out.print(valB + " ");
				}
			}
			System.out.println();
		}
		System.out.println("==============================================");
		System.out.println("==============================================");
	}
}
