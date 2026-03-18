# SupportAI Chat Agents

A Spring Boot app that routes user messages between two AI agents depending on what the user is asking about.

- **Technical Specialist** — answers technical questions based on a set of local documentation files. Won't guess — if the answer isn't in the docs, it says so.
- **Billing Specialist** — handles billing questions by calling backend tools (refund requests, plan details, billing history).

Conversations are multi-turn and the system switches agents automatically mid-conversation when the topic changes.

---

## Prerequisites

- Java 17+
- Maven 3.8+
- Git
- Postman or curl if you want to test manually

---

## Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/Kyadda99/supportai-chat-agents.git
cd supportai-chat-agents
```

### 2. Set your API credentials

Edit `src/main/resources/application.properties`:
```properties
llm.api-key=YOUR_API_KEY
llm.api-url=YOUR_API_URL
```

For Gemini Flash the URL looks like this:
```
https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite-preview:generateContent
```

### 3. Run it
```bash
mvn spring-boot:run
```

Starts on http://localhost:8080.

---

## Sending messages

POST `/chat`
```json
{
  "conversationId": "15273368-5c49-438b-b100-2e330a54c59d",
  "message": "I think I was charged twice this month."
}
```

Leave `conversationId` empty to start a fresh conversation, system will generate an ID for you.

### Want to pick up where you left off?

These conversations are already saved and ready to continue:
```
2645c4ed-3aaa-4afb-b3c8-7b2d8b2808bf
15273368-5c49-438b-b100-2e330a54c59d
a822349b-034d-4ff5-a6f1-63c2340ffd9e
ab6a4997-8dbd-451e-95b8-588aceaaecf8
dd2f7711-4687-4c54-b926-4c78f5e34adc
```

---

## Conversation logs

Saved to `/conversations` at the project root.



## Documentation files (used by the Technical Specialist)

- `text1.pdf` — generating API keys, auth headers, common errors
- `webhook_configuration_guide.pdf` — setting up webhooks, supported events, troubleshooting
- `text3.pdf` — default limits, what happens when you exceed them, how to request an increase
