
public class Test {

	public static void main(String[] args) {
		Mickey mickey = new Mickey();
		
		String key = "hola";
		String input = "Texto claro";
		String output;
		
		/*Pasos a seguir
		 * 
		 * 
		 * 1- Initialise the registers R and S with all zeros.
		 * 2- (Load in IV .)
		 * 3- (Load in K .) 
		 * 4- (Preclock.)
		 * 5- Encrypt
		 * 
		 */
		
		//Paso 1
		Ctx ctx = new Ctx();//Inicializa to oen 0
		
		//Paso 2 y 3
		mickey.ECRYPT_keysetup(ctx, key.getBytes(), key.length(), ivsize);;
		mickey.ECRYPT_ivsetup(ctx, iv);
		
		//Encriptar /* 0 = encrypt; 1 = decrypt; */
		mickey.ECRYPT_process_bytes(0, ctx, input.getBytes(), output.getBytes(), input.length());
		
		

	}

}
