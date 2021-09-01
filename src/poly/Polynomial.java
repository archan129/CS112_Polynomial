package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly1 == null) {
			Node ref = new Node(0, 0, null);
			Node r = ref;
			for (Node ptr = poly2; ptr != null; ptr = ptr.next) {
				ref.next = new Node(ptr.term.coeff, ptr.term.degree, null);
				ref = ref.next;
			}
			return r.next;
		}
		if (poly2 == null) {
			Node ref = new Node(0, 0, null);
			Node r = ref;
			for (Node ptr = poly2; ptr != null; ptr = ptr.next) {
				ref.next = new Node(ptr.term.coeff, ptr.term.degree, null);
				ref = ref.next;
			}
			return r.next;
		}
		Node polyF = new Node(0, 0, null);
		Node pH = polyF;
		while (poly1 != null && poly2 != null) {

			if (poly2.term.degree > poly1.term.degree) {
				polyF.next = new Node(poly1.term.coeff, poly1.term.degree, null);
				poly1 = poly1.next;
				polyF = polyF.next;

			} else if (poly1.term.degree > poly2.term.degree) {
				polyF.next = new Node(poly2.term.coeff, poly2.term.degree, null);
				;
				poly2 = poly2.next;
				polyF = polyF.next;

			} else {

				polyF.next = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree, null);
				polyF = polyF.next;
				poly1 = poly1.next;
				poly2 = poly2.next;
			}

		}
		while (poly1 != null) {
			polyF.next = new Node(poly1.term.coeff, poly1.term.degree, null);
			poly1 = poly1.next;
			polyF = polyF.next;
		}

		while (poly2 != null) {
			polyF.next = new Node(poly2.term.coeff, poly2.term.degree, null);
			poly2 = poly2.next;
			polyF = polyF.next;
		}

		polyF = pH;
		while (polyF.next != null) {
			if (polyF.next.term.coeff == 0) {
				polyF.next = polyF.next.next;
			} else {
				polyF = polyF.next;
			}

		}
		
		return simplify(pH.next);
	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		if (poly1 == null) {
			return null;
		}
		if (poly2 == null) {
			return null;
		}

		Node polyF = new Node(0, 0, null);
		Node pH = polyF;
		Node pH2 = poly2;

		while (poly1 != null) {
			while (poly2 != null) {
				polyF.next = new Node(poly1.term.coeff * poly2.term.coeff, poly1.term.degree + poly2.term.degree, null);
				poly2 = poly2.next;
				polyF = polyF.next;
			}
			poly1 = poly1.next;
			poly2 = pH2;
		}

		Node ne = pH;
		while (ne.next != null) {
			if (ne.next.term.coeff == 0) {
				ne.next = ne.next.next;
			} else {
				ne = ne.next;
			}
		}

		pH = simplify(pH);

		return pH;

	}

	// Simplifies polynomial by adding like terms
	private static Node simplify(Node n) {
		Node polyF = null;

		for (Node ptr = n; ptr != null; ptr = ptr.next) {
			float sum = ptr.term.coeff;
			for (Node ptr2 = ptr.next; ptr2 != null; ptr2 = ptr2.next) {

				if (ptr2.term.degree == -1) {
					continue;
				}

				if (ptr.term.degree == ptr2.term.degree) {
					sum += ptr2.term.coeff;
					ptr2.term.degree = -1;
				}
			}

			if (ptr.term.degree != -1) {
				polyF = new Node(sum, ptr.term.degree, polyF);
			}
		}

		if (polyF.term.coeff == 0 && polyF.next == null) {
			return null;
		} else {
			return sort(polyF);
		}
	}

	// reverses the linked list
	private static Node reverseLL(Node nod) {
		Node s = null;
		for (Node ptr = nod; ptr != null; ptr = ptr.next) {
			s = new Node(ptr.term.coeff, ptr.term.degree, s);
		}
		return s;
	}

	private static Node sort(Node poly) {
		// Counts how many nodes are joined with given parameter.
		Node n = new Node(0, 0, null);
		Node h = n;
		float d = 0;
		int count = 0;
		// finds highest degree and number of nodes
		for (Node f = poly; f != null; f = f.next) {
			if (f.term.degree > d) {
				d = f.term.degree;
			}
			count += 1;
		}
		while (d >= 0) {
			for (Node f2 = poly; f2 != null; f2 = f2.next) {
				if (f2.term.degree == d) {
					n.next = new Node(f2.term.coeff, f2.term.degree, null);
					n = n.next;
				} else {
					continue;
				}

			}
			d--;
			count--;
		}
		return reverseLL(h.next);
	}

	// easy print
	private static void pln(String a) {
		System.out.println(a);
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x    Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float sum = 0;
		for (Node f = poly; f != null; f = f.next) {
			sum += (f.term.coeff * Math.pow(x, f.term.degree));

		}
		return sum;
	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
