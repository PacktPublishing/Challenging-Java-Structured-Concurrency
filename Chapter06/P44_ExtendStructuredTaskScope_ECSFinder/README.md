# Extending StructuredTaskScope
Beside the built-in joiners and `allUntil()`, we can implement our custom policies by implementing the `StructuredTaskScope.Joiner`
 interface and overriding the needed methods (`onComplete()`, `result()`, and so on). Provide an example of extending the `StructuredTaskScope`.