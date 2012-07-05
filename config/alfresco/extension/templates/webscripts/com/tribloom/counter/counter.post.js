<import resource="classpath:alfresco/extension/templates/webscripts/com/tribloom/counter/counter.js">

function main() {
	var name = url.templateArgs.name;
	var counterNode = getCounter(name);
	if (counterNode == null) {
		status.setCode(
			status.STATUS_NOT_FOUND, // 404
			"Counter not found with name " + name
		);
		return;
	}
	if (requestbody.content.isEmpty()) {
		status.setCode(
			status.STATUS_BAD_REQUEST, // 400
			"No data posted"
		);
		return;
	}
	var value = 0;
	var json = jsonUtils.toObject(requestbody.content);
	if (json != null && typeof json === "object" && typeof json.value === "number") {
		value = json.value;
	} else {
		// Otherwise attempt to parse it as a number
		var testValue = parseInt(requestbody.content);
		if (!isNaN(testValue)) {
			value = testValue;
		}
	}
	counterNode.properties["dm:count"] = value;
	counterNode.save();
	status.setCode(
		status.STATUS_OK, // 200
		"Updated " + name + " counter with value " + value
	);
}
main();