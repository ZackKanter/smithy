/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package software.amazon.smithy.model.traits;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.model.shapes.IntEnumShape;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.shapes.ShapeId;

public class TitleTraitTest {
    @Test
    public void loadsTraitWithString() {
        Node node = Node.from("Title");
        TraitFactory provider = TraitFactory.createServiceFactory();
        Optional<Trait> trait = provider.createTrait(
                ShapeId.from("smithy.api#title"),
                ShapeId.from("ns.qux#foo"),
                node);

        assertTrue(trait.isPresent());
        assertThat(trait.get(), instanceOf(TitleTrait.class));
        TitleTrait titleTrait = (TitleTrait) trait.get();
        assertThat(titleTrait.getValue(), equalTo("Title"));
        assertThat(titleTrait.toNode(), equalTo(node));
    }

    @Test
    public void canBeAppliedToEnumMembers() {
        Model model = Model.assembler()
                .addUnparsedModel("test.smithy",
                        "$version: \"2.0\"\n"
                                + "namespace smithy.example\n"
                                + "enum Status {\n"
                                + "    @title(\"Pending Status\")\n"
                                + "    PENDING\n"
                                + "}\n")
                .assemble()
                .unwrap();

        EnumShape shape = model.expectShape(ShapeId.from("smithy.example#Status"), EnumShape.class);
        MemberShape member = shape.getMember("PENDING").get();
        assertTrue(member.hasTrait(TitleTrait.class));
        assertThat(member.expectTrait(TitleTrait.class).getValue(), equalTo("Pending Status"));
    }

    @Test
    public void canBeAppliedToIntEnumMembers() {
        Model model = Model.assembler()
                .addUnparsedModel("test.smithy",
                        "$version: \"2.0\"\n"
                                + "namespace smithy.example\n"
                                + "intEnum Priority {\n"
                                + "    @title(\"Low Priority\")\n"
                                + "    LOW = 1\n"
                                + "}\n")
                .assemble()
                .unwrap();

        IntEnumShape shape = model.expectShape(ShapeId.from("smithy.example#Priority"), IntEnumShape.class);
        MemberShape member = shape.getMember("LOW").get();
        assertTrue(member.hasTrait(TitleTrait.class));
        assertThat(member.expectTrait(TitleTrait.class).getValue(), equalTo("Low Priority"));
    }
}
