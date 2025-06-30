# Kipon 

![Kipon cover](./app/assets/images/Kipon_github_cover.jpg)

> 📘 [Versión en Español / Spanish version](README_ES.md)

**Kipon** is an Android application developed as the final project for the DAM superior grade.  
The idea emerged from a personal need: to save money collaboratively between more than two people — whether for personal goals (such as trips, gifts, or events) or professional ones (like funding a startup or a joint project)..

Although there are solutions for individual saving or shared expense management, Kipon focuses on creating **shared savings jars**, simplifying group financial organization and promoting transparency.

This repository corresponds to a **first functional prototype** of the application, developed with the aim of validating the idea, testing technical feasibility, and laying the groundwork for future, more complete versions.

The app is developed in `Kotlin` using `Jetpack Compose`, `Material Design`, a `Clean Architecture` structure with an `MVVM` presentation model, and libraries compatible with `KMP` (Kotlin Multiplatform).

## Requirements
![Kotlin](https://img.shields.io/badge/kotlin-blue) ![Jetpack](https://img.shields.io/badge/Jetpack-gray) ![Android SDK](https://img.shields.io/badge/API-28+-green)

- Language: Kotlin
- Toolkit: Jetpack, Koin, Ktor
- Minimum SDK: 28
- Target SDK: 35

---

## Technologies and Libraries

- **Jetpack Compose**:  Modern Android UI toolkit for building declarative interfaces quickly and efficiently.
- **ViewModel**: Manages and retains UI-related data in a lifecycle-conscious way.
- **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata?hl=es-419#create_livedata_objects)**: Observes data and allows the UI to automatically react to changes.
- **[Navigation Compose](https://developer.android.com/develop/ui/compose/navigation?hl=es-419)**: Enables navigation between screens.
- **[Ktor](https://ktor.io/docs/client-create-multiplatform-application.html)**: Multiplatform HTTP client for consuming REST APIs.
- **[Koin](https://insert-koin.io/docs/reference/koin-mp/kmp/)**: Lightweight dependency injection framework with multiplatform support.
- **[Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization)**: Reflection-free, multiplatform, and multi-format serialization.
- **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)**: Simplifies asynchronous and concurrent programming, allowing for non-blocking operations like network or database calls.

---

## Architecture

- **Clean Architecture** with **MVVM (Model - ViewModel - View)** presentation model:
  The project is structured following Clean Architecture principles to improve maintainability, testability, and scalability. Business logic is decoupled from presentation logic, enabling a modular and responsibility-focused development.

### Data Layer
- **API**: Data fetched from a REST API using Ktor.
- **Repositories**: Interfaces that define data access operations, implemented to interact with the REST API and local database.

### Domain Layer
- **Bussiness Logic**: Implementation of use cases required to carry out the developed features.

### Presentation Layer
- **ViewModels**: Classes that handle UI logic and expose data to the UI using LiveData.
- **Composables**: Declarative UI components built with Jetpack Compose that represent views and react to data changes.
- **Screens**: App screens that use composables to display information to the user.

---

## REST API
The app uses a custom REST API developed in Java with Spring, and integrates with `AWS services`.  
You can access the backend code in the following repository:  : [Kipon API](https://github.com/AnnaSolox/kipon-api)

---

## LICENSE
Designed and developed by AnnaSolox in 2025.

This project is licensed under the [Apache 2.0](./LICENSE).
You may not use this file except in compliance with the License.  
You may obtain a copy of the License at:

https://opensource.org/license/apache-2-0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

