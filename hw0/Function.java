public class Function{

	public int max(int[] a){
		int temp = a[0];
		for(int i =1; i < a.length; i++){
			if (a[i] > temp){
				temp = a[i];
			}
		}
		return temp;
	}

	public boolean threeSum(int[] a){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a.length; j++){
				for(int k = 0; k < a.length; k++){
					if(a[i] + a[j] + a[k] == 0){
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public boolean threeSumDistinct(int[] a){
		for(int i = 0; i < a.length; i++){
			for(int j = i+1; j < a.length; j++){
				for(int k = j+1; k < a.length; k++){
					if(a[i] + a[j] + a[k] == 0){
						return true;
					}
				}
			}
		}
		
		return false;
	}

}