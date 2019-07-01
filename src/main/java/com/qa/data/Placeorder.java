  package com.qa.data;
  import java.util.List;

        

	    public class Placeorder {
        public List stops;
        public String orderAt;

		public String getOrderAt() {
			return orderAt;
		}


		public void setOrderAt(String orderAt) {
			this.orderAt = orderAt;
		}


		public List getstops(){
		return this.stops;
		}
		

		public void setstops(List stops){
		this.stops = stops;
		}
			

	    public Placeorder() {
			
		}	

		
		
	    }


