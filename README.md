Send NEO:
```java
String transactionHash = new Transaction.Builder()
        .node("http://seed7.ngd.network:10332")
        .from("L4Kk8QZwZSeQjdpVF2FC74zgNs481GphVVXkHJTazYpy5rBV5mKf")
        .amount(10)
        .neo()
        .to("AKDVzYGLczmykdtRaejgvWeZrvdkVEvQ1X")
        .build()
        .send();
```

Send NEP-5 tokens:
```java
String transactionHash = new Transaction.Builder("http://seed7.ngd.network:10332")
        .from("L4Kk8QZwZSeQjdpVF2FC74zgNs481GphVVXkHJTazYpy5rBV5mKf")
        .amount("1000.375")
        .nep5("7f86d61ff377f1b12e589a5907152b57e2ad9a7a")
        .to("AKDVzYGLczmykdtRaejgvWeZrvdkVEvQ1X")
        .build()
        .send();
```