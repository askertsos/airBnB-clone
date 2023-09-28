const downloadDatabaseJSON = async () => {
	const fetchOptions = {
		headers: {
			"Content-type": "application/json",
			Authorization: "Bearer " + localStorage.getItem("jwt"),
			Accept: "application/json",
		},
		method: "GET",
	};
	fetch("https://localhost:8080/admin/json", fetchOptions)
		.then((response) => {
			console.log(response);
			return response.json();
		})
		.then((data) => exportDatabaseToJSON(data));
};

const downloadDatabaseXML = async () => {
	const fetchOptions = {
		headers: {
			"Content-type": "application/json",
			Authorization: "Bearer " + localStorage.getItem("jwt"),
			Accept: "application/xml",
		},
		method: "GET",
	};

	fetch("https://localhost:8080/admin/xml", fetchOptions)
		.then((response) => response.text())
		.then((data) => exportDatabaseToXML(data));
};

const exportDatabaseToJSON = (data) => {
	const blob = new Blob([JSON.stringify(data, null, 2)], {
		type: "application/json",
	});
	const url = URL.createObjectURL(blob);
	const link = document.createElement("a");
	link.setAttribute("download", "database.json");
	document.body.appendChild(link);
	link.href = url;
	link.click();
	link.remove();

	window.location.reload(false);
};

const exportDatabaseToXML = (data) => {
	const xmlDoc = new DOMParser().parseFromString(data, "application/xml");
	const xmlSerializer = new XMLSerializer();
	const xmlString = xmlSerializer.serializeToString(xmlDoc);

	const blob = new Blob([xmlString], {
		type: "application/xml",
	});
	const url = URL.createObjectURL(blob);
	const link = document.createElement("a");
	link.download = "database.xml";
	link.href = url;
	link.click();
	link.remove();

	window.location.reload(false);
};
export { downloadDatabaseJSON, downloadDatabaseXML };
