/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhalcojor.oompaloompas.data.di

import com.dhalcojor.oompaloompas.data.DefaultOompaLoompasListRepository
import com.dhalcojor.oompaloompas.data.OompaLoompasListRepository
import com.dhalcojor.oompaloompas.ui.oompaloompaslist.OompaLoompaListItemState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsOompaLoompasListRepository(
        oompaLoompasListRepository: DefaultOompaLoompasListRepository
    ): OompaLoompasListRepository
}

val fakeOompaLoompasLists = listOf(
    OompaLoompaListItemState("Marcy", "Karadzas"),
    OompaLoompaListItemState("Kotlin", "Android"),
)