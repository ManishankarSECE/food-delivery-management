package fdms;


public class FoodItem {
    
	
    

	private String name;
    private String category;
    private double price;
    private String description;
    private boolean availability;

    public FoodItem( String name, String category, double price, String description, boolean availability) {
        
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.availability = availability;
    }

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAvailability() {
		return availability;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
}
