const request = require('request');
const cheerio = require('cheerio');
const fs = require('fs');
// MHC Menu Item Page (test)
const stationPage = 'https://menu.mtholyoke.edu/shortmenu.aspx?sName=Mount+Holyoke+College+Dining+Services&locationNum=40&locationName=Classics&naFlag=1&dtdate=11%2f02%2f2018&mealName=Lunch'
// MHC Menu Start Page
const startPage = 'https://menu.mtholyoke.edu/';	
const dietary = { DAIRY : 0, EGGS : 1, FISH : 2, SHELLFISH : 3, TREE_NUTS : 4, PEANUTS : 5, SOYBEANS : 6, SESAME : 7, 
		GLUTEN : 8, COCONUT : 9, BEEF : 10, LAMB : 11, PORK : 12, POULTRY : 13, VEGETARIAN : 14, VEGAN : 15 };
// A dummy for the database
var database = {
		"breakfast" : {
	"entrees" : [],
	"sides" : [],
	"soups" : []
},
		"lunch" : {
	"entrees" : [],
	"sides" : [],
	"soups" : []
},
		"dinner" : {
	"entrees" : [],
	"sides" : [],
	"soups" : []
}
}


// The data scraping script
request(stationPage, (error, response, html) => {
	if (!error && response.statusCode == 200) {
		let $ = cheerio.load(html);
		// Save the name of the station
		let stationName = $('.loc-head').text().replace(" -", " - ");

		// For each mealtime, if it exists
		$('.tab-pane').each((mealNum, meal) => {
			let mealTime = $(meal).find('.meal-list');
			if ($(mealTime).text().trim() != "") {
				// Get every section in this mealtime and parse it to items
				$(mealTime).find('.shortmenucats').each((sectioNum, section) => {
					// Get the name of the section
					let sectionName = $(section).text().toLowerCase();

					// Determine the appropriate category for this section's items
					if (sectionName.includes("entree") || sectionName.includes("entee")) {
						// Get every item in this section and put it in the database
						$(mealTime).find('.shortmenurecipes').each((recipeNum, recipe) => {
							$(recipe).each((iter, thing) => {
								// Create the dietary restrictions array
								let restrictions = Array(16).fill(0);
								$(thing).find('img').each((imgNum, dietary) => {
									let name = $(dietary).attr('src');
									//console.log(name);
									if (name.includes("Dairy")) restrictions[dietary.DAIRY] = 1;
									else if (name.includes("Eggs")) restrictions[dietary.EGGS] = 1;
									else if (name.includes("Shell")) restrictions[dietary.SHELLFISH] = 1;
									else if (name.includes("Fish")) restrictions[dietary.FISH] = 1;
									else if (name.includes("Tree")) restrictions[dietary.TREE_NUTS] = 1;
									else if (name.includes("Peanuts")) restrictions[dietary.PEANUTS] = 1;
									else if (name.includes("Soy")) restrictions[dietary.SOYBEANS] = 1;
									else if (name.includes("Sesame")) restrictions[dietary.SESAME] = 1;
									else if (name.includes("Gluten")) restrictions[dietary.GLUTEN] = 1;
									else if (name.includes("Coconut")) restrictions[dietary.COCONUT] = 1;
									else if (name.includes("Beef")) restrictions[dietary.BEEF] = 1;
									else if (name.includes("Lamb")) restrictions[dietary.LAMB] = 1;
									else if (name.includes("Pork")) restrictions[dietary.PORK] = 1;
									else if (name.includes("Poultry")) restrictions[dietary.POULTRY] = 1;
									else if (name.includes("Vegetarian")) restrictions[dietary.VEGETARIAN] = 1;
									else if (name.includes("Vegan")) restrictions[dietary.VEGAN] = 1;
								});
								let hrefValue = $(thing).find('a').attr('href');
								request(startPage + hrefValue, (error, response, html) => {
									if (!error && response.statusCode == 200) {
										let $ = cheerio.load(html);
										let totalFat, saturatedFat, transFat, totalCarbs, 
										sugars, cholesterol, protein, sodium;

										// Parse the nutritional elements
										$('.label-thick-border tbody td').each((nutrNum, nutrElem) => {
											if (!(nutrNum % 2)) {
												let str = $(nutrElem).text().trim().split(" ");
												let number;
												switch (str[0]) {
												case ("Total") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													if (str[1] == "Fat") totalFat = number;					
													else totalCarbs = number;
													break;
												}						
												case ("Saturated") : {
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													saturatedFat = number;
													break;
												}
												case ("Trans") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;				
													transFat = number;
													break;
												}
												case ("Sugars") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sugars = number;
													break;
												}
												case ("Cholesterol") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													cholesterol = number;
													break;
												}
												case ("Protein") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													protein = number;
													break;
												}
												case ("Sodium") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sodium = number;
													break;
												}
												}					
											}
										});

										// Build the item
										const item = {
												"name" : $('.label-head').text().trim(),
												"stationName" : stationName,
												"serving" : $('.serving').text().trim().substring(13).toLowerCase(),
												"calories" : parseInt($('.label-calories').text().trim().
														split("\n")[0].split(" ")[1]),
												"totalFat" : totalFat,
												"saturatedFat" : saturatedFat,
												"transFat" : transFat,
												"totalCarbs" : totalCarbs,
												"sugars" : sugars,
												"cholesterol" : cholesterol,
												"protein" : protein,
												"sodium" : sodium,
												"restrictions" : restrictions
										}	
										// Push the item into the database
										if (mealNum == 0) database.breakfast.entrees.push(item);
										else if (mealNum == 1) database.lunch.entrees.push(item);
										else database.dinner.entrees.push(item);
										fs.writeFileSync('./data.json', JSON.stringify({"items" : database}, null, 4), 'utf-8');					
									}
								});
							});									
						});		
					}
					else if (sectionName.includes("side") || sectionName.includes("vegetable") ||
							sectionName.includes("starch") || sectionName.includes("condiment")) {
						// Get every item in this section and put it in the database
						$(mealTime).find('.shortmenurecipes').each((recipeNum, recipe) => {
							$(recipe).each((iter, thing) => {
								// Create the dietary restrictions array
								let restrictions = Array(16).fill(0);
								$(thing).find('img').each((imgNum, dietary) => {
									let name = $(dietary).attr('src');
									if (name.includes("Dairy")) restrictions[dietary.DAIRY] = 1;
									else if (name.includes("Eggs")) restrictions[dietary.EGGS] = 1;
									else if (name.includes("Shell")) restrictions[dietary.SHELLFISH] = 1;
									else if (name.includes("Fish")) restrictions[dietary.FISH] = 1;
									else if (name.includes("Tree")) restrictions[dietary.TREE_NUTS] = 1;
									else if (name.includes("Peanuts")) restrictions[dietary.PEANUTS] = 1;
									else if (name.includes("Soy")) restrictions[dietary.SOYBEANS] = 1;
									else if (name.includes("Sesame")) restrictions[dietary.SESAME] = 1;
									else if (name.includes("Gluten")) restrictions[dietary.GLUTEN] = 1;
									else if (name.includes("Coconut")) restrictions[dietary.COCONUT] = 1;
									else if (name.includes("Beef")) restrictions[dietary.BEEF] = 1;
									else if (name.includes("Lamb")) restrictions[dietary.LAMB] = 1;
									else if (name.includes("Pork")) restrictions[dietary.PORK] = 1;
									else if (name.includes("Poultry")) restrictions[dietary.POULTRY] = 1;
									else if (name.includes("Vegetarian")) restrictions[dietary.VEGETARIAN] = 1;
									else if (name.includes("Vegan")) restrictions[dietary.VEGAN] = 1;
								});
								let hrefValue = $(thing).find('a').attr('href');
								request(startPage + hrefValue, (error, response, html) => {
									if (!error && response.statusCode == 200) {
										let $ = cheerio.load(html);
										let totalFat, saturatedFat, transFat, totalCarbs, 
										sugars, cholesterol, protein, sodium;

										// Parse the nutritional elements
										$('.label-thick-border tbody td').each((nutrNum, nutrElem) => {
											if (!(nutrNum % 2)) {
												let str = $(nutrElem).text().trim().split(" ");
												let number;
												switch (str[0]) {
												case ("Total") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													if (str[1] == "Fat") totalFat = number;					
													else totalCarbs = number;
													break;
												}						
												case ("Saturated") : {
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													saturatedFat = number;
													break;
												}
												case ("Trans") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;				
													transFat = number;
													break;
												}
												case ("Sugars") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sugars = number;
													break;
												}
												case ("Cholesterol") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													cholesterol = number;
													break;
												}
												case ("Protein") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													protein = number;
													break;
												}
												case ("Sodium") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sodium = number;
													break;
												}
												}					
											}
										});

										// Build the item
										const item = {
												"name" : $('.label-head').text().trim(),
												"stationName" : stationName,
												"serving" : $('.serving').text().trim().substring(13).toLowerCase(),
												"calories" : parseInt($('.label-calories').text().trim().
														split("\n")[0].split(" ")[1]),
												"totalFat" : totalFat,
												"saturatedFat" : saturatedFat,
												"transFat" : transFat,
												"totalCarbs" : totalCarbs,
												"sugars" : sugars,
												"cholesterol" : cholesterol,
												"protein" : protein,
												"sodium" : sodium,
												"restrictions" : restrictions
										}	
										// Push the item into the database
										if (mealNum == 0) database.breakfast.sides.push(item);
										else if (mealNum == 1) database.lunch.sides.push(item);
										else database.dinner.sides.push(item);
									}
								});								
							});									
						});
					}
					else if (sectionName.includes("soup")) {
						// Get every item in this section and put it in the database
						$(mealTime).find('.shortmenurecipes').each((recipeNum, recipe) => {
							$(recipe).each((iter, thing) => {
								// Create the dietary restrictions array		
								let restrictions = Array(16).fill(0);
								$(thing).find('img').each((imgNum, dietary) => {
									let name = $(dietary).attr('src');
									if (name.includes("Dairy")) restrictions[dietary.DAIRY] = 1;
									else if (name.includes("Eggs")) restrictions[dietary.EGGS] = 1;
									else if (name.includes("Shell")) restrictions[dietary.SHELLFISH] = 1;
									else if (name.includes("Fish")) restrictions[dietary.FISH] = 1;
									else if (name.includes("Tree")) restrictions[dietary.TREE_NUTS] = 1;
									else if (name.includes("Peanuts")) restrictions[dietary.PEANUTS] = 1;
									else if (name.includes("Soy")) restrictions[dietary.SOYBEANS] = 1;
									else if (name.includes("Sesame")) restrictions[dietary.SESAME] = 1;
									else if (name.includes("Gluten")) restrictions[dietary.GLUTEN] = 1;
									else if (name.includes("Coconut")) restrictions[dietary.COCONUT] = 1;
									else if (name.includes("Beef")) restrictions[dietary.BEEF] = 1;
									else if (name.includes("Lamb")) restrictions[dietary.LAMB] = 1;
									else if (name.includes("Pork")) restrictions[dietary.PORK] = 1;
									else if (name.includes("Poultry")) restrictions[dietary.POULTRY] = 1;
									else if (name.includes("Vegetarian")) restrictions[dietary.VEGETARIAN] = 1;
									else if (name.includes("Vegan")) restrictions[dietary.VEGAN] = 1;
								});
								let hrefValue = $(thing).find('a').attr('href');
								request(startPage + hrefValue, (error, response, html) => {
									if (!error && response.statusCode == 200) {
										let $ = cheerio.load(html);
										let totalFat, saturatedFat, transFat, totalCarbs, 
										sugars, cholesterol, protein, sodium;

										// Parse the nutritional elements
										$('.label-thick-border tbody td').each((nutrNum, nutrElem) => {
											if (!(nutrNum % 2)) {
												let str = $(nutrElem).text().trim().split(" ");
												let number;
												switch (str[0]) {
												case ("Total") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													if (str[1] == "Fat") totalFat = number;					
													else totalCarbs = number;
													break;
												}						
												case ("Saturated") : {
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;
													saturatedFat = number;
													break;
												}
												case ("Trans") : {	
													number = parseFloat(str[2]);
													if (isNaN(number) == true) number = 0;				
													transFat = number;
													break;
												}
												case ("Sugars") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sugars = number;
													break;
												}
												case ("Cholesterol") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													cholesterol = number;
													break;
												}
												case ("Protein") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													protein = number;
													break;
												}
												case ("Sodium") : {
													number = parseFloat(str[1]);
													if (isNaN(number) == true) number = 0;	
													sodium = number;
													break;
												}
												}					
											}
										});

										// Build the item
										const item = {
												"name" : $('.label-head').text().trim(),
												"stationName" : stationName,
												"serving" : $('.serving').text().trim().substring(13).toLowerCase(),
												"calories" : parseInt($('.label-calories').text().trim().
														split("\n")[0].split(" ")[1]),
												"totalFat" : totalFat,
												"saturatedFat" : saturatedFat,
												"transFat" : transFat,
												"totalCarbs" : totalCarbs,
												"sugars" : sugars,
												"cholesterol" : cholesterol,
												"protein" : protein,
												"sodium" : sodium,
												"restrictions" : restrictions
										}	

										// Push the item into the database
										if (mealNum == 0) database.breakfast.soups.push(item);
										else if (mealNum == 1) database.lunch.soups.push(item);
										else database.dinner.soups.push(item);
									}									
								});								
							});									
						});
					}
				});
			}
		});
	}
});	


// Parse item
async function parseItem(link, restrictions, sectionName, stationName) {
	return new Promise(resolve => {
		request(link, (error, response, html) => {
			console.log("item");
			if (!error && response.statusCode == 200) {
				let $ = cheerio.load(html);
				let totalFat, saturatedFat, transFat, totalCarbs, 
				sugars, cholesterol, protein, sodium;

				// Parse the nutritional elements
				$('.label-thick-border tbody td').each((nutrNum, nutrElem) => {
					if (!(nutrNum % 2)) {
						let str = $(nutrElem).text().trim().split(" ");
						let number;
						switch (str[0]) {
						case ("Total") : {	
							number = parseFloat(str[2]);
							if (isNaN(number) == true) number = 0;
							if (str[1] == "Fat") totalFat = number;					
							else totalCarbs = number;
							break;
						}						
						case ("Saturated") : {
							number = parseFloat(str[2]);
							if (isNaN(number) == true) number = 0;
							saturatedFat = number;
							break;
						}
						case ("Trans") : {	
							number = parseFloat(str[2]);
							if (isNaN(number) == true) number = 0;				
							transFat = number;
							break;
						}
						case ("Sugars") : {
							number = parseFloat(str[1]);
							if (isNaN(number) == true) number = 0;	
							sugars = number;
							break;
						}
						case ("Cholesterol") : {
							number = parseFloat(str[1]);
							if (isNaN(number) == true) number = 0;	
							cholesterol = number;
							break;
						}
						case ("Protein") : {
							number = parseFloat(str[1]);
							if (isNaN(number) == true) number = 0;	
							protein = number;
							break;
						}
						case ("Sodium") : {
							number = parseFloat(str[1]);
							if (isNaN(number) == true) number = 0;	
							sodium = number;
							break;
						}
						}					
					}
				});

				// Build the item
				const item = {
						"name" : $('.label-head').text().trim(),
						"stationName" : stationName,
						"serving" : $('.serving').text().trim().substring(13).toLowerCase(),
						"calories" : parseInt($('.label-calories').text().trim().
								split("\n")[0].split(" ")[1]),
						"totalFat" : totalFat,
						"saturatedFat" : saturatedFat,
						"transFat" : transFat,
						"totalCarbs" : totalCarbs,
						"sugars" : sugars,
						"cholesterol" : cholesterol,
						"protein" : protein,
						"sodium" : sodium,
						"restrictions" : restrictions
				}	

				// Push the item into the database
				/*if (mealNum == 0) database.breakfast.soups.push(item);
												else if (mealNum == 1) database.lunch.soups.push(item);
												else database.dinner.soups.push(item);*/
				console.log("write");
			}									
		});
	});
}