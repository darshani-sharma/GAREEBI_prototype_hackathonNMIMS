#GAREEBI_prototype_hackathonNMIMS
A decentralized P2P marketplace connecting renewable energy prosumers and consumers through IoT smart meters and secure automated trading to drive community led sustainability and transparent carbon tracking.

Renewable Energy Marketplace Portal
Connecting Prosumers, Consumers, and Investors for a Greener Tomorrow.


Project Overview
The Renewable Energy Marketplace is a decentralized digital platform designed to bridge the gap between green energy producers (prosumers) and those looking to consume or invest in sustainable power. By leveraging IoT and P2P trading, the platform removes the middleman, allowing a homeowner with solar panels to sell excess electricity directly to their neighbour or a local business.
Objectives
•	Decentralized Exchange: Move away from monolithic power grids toward local, resilient energy clusters.
•	P2P Trading: Facilitate direct financial and energy transactions between peers.
•	Community Sustainability: Provide a platform for collective investment in community wind farms or biogas plants.

Multilayered System Architecture
The platform operates on a 3-tier architecture to ensure real-time accuracy and secure financial settlements.
1. Smart Meters (User Layer)
The hardware foundation of the system. Every participant (producer or consumer) is equipped with an IoT-enabled smart meter.
•	Real-time Data Acquisition: Measures energy generation (input) and consumption (output) at millisecond intervals.
•	Edge Validation: Validates that the energy being sold actually exists in the local circuit before listing it on the marketplace.
•	Secure Communication: Uses protocols like MQTT or HTTPS to send encrypted telemetry data to the P2P service.

2. P2P Service (Transaction Layer)
This is the "brain" of the marketplace, often powered by Blockchain (Smart Contracts) to ensure trustless trading.
•	Automated Matching: An order matching engine that pairs sellers (e.g., a rooftop solar owner) with buyers based on price, proximity, and volume.
•	Smart Contracts: When a match is found, a contract automatically executes the trade, handling the digital wallet transfer and logging the "Energy Token" exchange.
•	Dynamic Pricing: Implements algorithms that adjust prices based on local demand/supply (e.g., lower prices during peak sunny hours).

3. Backend Monitoring (Management Layer)
The administrative and analytical powerhouse that ensures system health.
•	Grid Stability Monitoring: Tracks total load across the microgrid to prevent surges or blackouts.
•	Fraud Detection: Uses machine learning to identify "False Data Injection" detecting if a user is trying to spoof their smart meter readings to earn unearned credits.
•	Predictive Analytics: Forecasts upcoming energy production based on integrated weather APIs (Solar/Wind forecasts).

4. Human Impact and Global Urgency
This platform transforms passive consumers into "prosumers," granting individuals financial autonomy by allowing them to sell excess energy directly to their community. By removing corporate monopolies and opaque billing, it democratizes the grid, ensuring that energy prices are driven by local supply rather than corporate greed. However, the reality is stark: our current centralized infrastructure is a "single point of failure" prone to mass blackouts and systemic "greenwashing," where fossil fuel power is falsely sold as renewable. As carbon taxes rise and fossil fuels deplete, staying tethered to the old grid is a massive financial liability. Transitioning to this decentralized P2P marketplace isn't just a technical upgrade; it is a critical defense against energy poverty and a looming global climate crisis.
