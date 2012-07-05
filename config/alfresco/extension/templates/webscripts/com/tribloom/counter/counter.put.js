<import resource="classpath:alfresco/extension/templates/webscripts/com/tribloom/counter/counter.js">

function main() {
	var name = url.templateArgs.name;
	var counterNode = getCounter(name);
	if (counterNode != null) {
		status.setCode(
			status.STATUS_NOT_MODIFIED, // 304
			"Counter " + name + " already exists."
		);
		return;
	}
	var value = 0;
	if (!requestbody.content.isEmpty()) {
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
	}
	counterNode = countersFolder.createNode(name, "dm:counter");
	counterNode.properties["dm:count"] = value;
	counterNode.save();
	status.setCode(
		status.STATUS_CREATED, // 201
		"Created counter with name " + name
	);
}
main();