# KLAY
[![Build Status](https://travis-ci.com/ks-shim/klay.svg?branch=master)](https://travis-ci.com/ks-shim/klay)
[![Coverage Status](https://coveralls.io/repos/github/ks-shim/klay/badge.svg?branch=master)](https://coveralls.io/github/ks-shim/klay?branch=master)
[ ![Download](https://api.bintray.com/packages/dwayne/nlp/klay/images/download.svg) ](https://bintray.com/dwayne/nlp/klay/_latestVersion)

**K**orean **L**anguage **A**nal**Y**zer using KOMORAN's dictionaries.
- korean morphology analysis
- 한국어 형태소 분석기 입니다.
- 개발 시작일 : 2019. 02 ~
  - version : 0.1 (2019.02.26)
- KOMORAN의 사전을 기반으로 분석하며, 사용하는 Data structure와 분석 방식은 상이합니다.
- Data Structure : KLAY의 분석 방식에 맞게 수정한 Lucene의 Trie를 사용합니다.
- KLAY is a thread-safe analyzer. (멀티 쓰레드 환경에서의 사용을 권장합니다.)


# Architecture
Performance와 동시에 확장성을 고려하였습니다. 그래서 조금 더 자바(Java)스럽게 Design하였습니다.

# Example
```java
    //***********************************************************************
    // 1. configuration and creating Klay object ...
    //***********************************************************************
    Klay klay = new Klay(Paths.get("data/configuration/klay.conf"));

    //***********************************************************************
    // 2. start morphological analysis.
    //***********************************************************************
    String text = "너무기대안하고갔나....................재밌게봤다";
    Morphs morphs = klay.doKlay(text);

    //***********************************************************************
    // 3. print result.
    //***********************************************************************
    Iterator<Morph> iter = morphs.iterator();
    while(iter.hasNext()) {
        System.out.println(iter.next());
    }
```
