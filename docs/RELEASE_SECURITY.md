# Release Security and Verification

## Published evidence

A release created by the project workflow includes:

- Maven POM, binary JAR, sources JAR, and Javadocs JAR;
- PGP signatures required by Maven Central;
- SHA-256 checksums;
- CycloneDX JSON SBOMs;
- GitHub/Sigstore artifact-attestation bundles;
- signed SLSA provenance in `.intoto.jsonl` format for discovery by OpenSSF
  Scorecard, retaining the complete Sigstore verification material;
- release notes identifying security changes.

## Verify Maven Central signatures

Download an artifact and its `.asc` signature from Maven Central, import the
maintainer's published key, and run:

```bash
gpg --verify artifact.jar.asc artifact.jar
```

Confirm the key fingerprint through an independent maintainer identity channel.

## Verify GitHub provenance

With GitHub CLI:

```bash
gh attestation verify artifact.jar \
  --repo ggomarighetti/rsql-jpa-search
```

Verification must identify this repository and the release workflow. Also
compare the artifact's SHA-256 digest with the release checksum manifest.

For verification using the downloaded release evidence:

```bash
gh attestation verify artifact.jar \
  --repo ggomarighetti/rsql-jpa-search \
  --bundle rsql-jpa-search-2.0.1-provenance.intoto.jsonl \
  --signer-workflow ggomarighetti/rsql-jpa-search/.github/workflows/release.yml \
  --deny-self-hosted-runners
```

The `.intoto.jsonl` file contains the same signed SLSA provenance bundle as
the `.sigstore.json` file, serialized as one JSON line. The release workflow
checks the staged artifacts against their checksums and verifies provenance
against the repository, workflow identity, and source commit before publishing.
Older releases can still produce Scorecard warnings because it examines
multiple recent releases and discovers provenance by the `.intoto.jsonl` suffix.

## Build from source

Use the source tag associated with the release:

```bash
./mvnw -Pcoverage,release verify
./mvnw -Ppublication,sbom -DskipTests deploy
```

The release tag must be signed and point to the commit identified by the
provenance.
